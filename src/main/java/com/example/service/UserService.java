package com.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MailService mailService;

    /**
     * 创造帐户
     *
     * @param user 用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Transactional
    public Map<String, Object> creatAccount(User user) {
        // 雪花算法生成确认码
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();
        // 生成 盐
        String salt = RandomUtil.randomString(6);
        // md5 加密
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        // 失效时间: 24h
        LocalDateTime activationTime = LocalDateTime.now().plusDays(1);
        // 加载用户信息
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        user.setConfirmCode(confirmCode);
        user.setActivationTime(activationTime);
        user.setIsValid((byte) 0);
        int result = userMapper.insertUser(user);
        Map<String, Object> resultMap = new HashMap<>();
        if (result > 0) {
            // 发送邮件
            String activationUrl = "http://localhost:8080/user/activation?confirmCode=" + confirmCode;
            mailService.sendMailForActivationAccount(activationUrl, user.getEmail());
            resultMap.put("code", 200);
            resultMap.put("message", "注册成功, 请前往邮箱进行账号激活");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "注册失败");
        }
        return resultMap;
    }

    public Map<String, Object> loginAccount(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> users = userMapper.selectPasswordByEmail(user.getEmail());
        // 判断账号是否存在
        if (users.isEmpty() || users == null) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号不存在或未激活");
            return resultMap;
        }
        // 判断账号是否多个
        if (users.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号异常, 请联系管理员处理");
            return resultMap;
        }
        // 判断密码是否正确
        User u = users.get(0);
        String md5Pwd = SecureUtil.md5(user.getPassword() + u.getSalt());
        if (!md5Pwd.equals(u.getPassword())) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号或密码错误");
            return resultMap;
        }
        // 登陆成功
        resultMap.put("code", 200);
        resultMap.put("message", "登陆成功");
        return resultMap;
    }

    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new HashMap<>();
        // 根据确认码查询用户
        User user = userMapper.selectActivationTimeByConfirmCode(confirmCode);
        // 判断激活是否超时
        boolean after = LocalDateTime.now().isAfter(user.getActivationTime());
        if (after) {
            resultMap.put("code", 200);
            resultMap.put("message", "激活失败，链接已失效，请重新注册！");
            return resultMap;
        }
        // 更改用户账号可用情况
        int result = userMapper.updateIsValidByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "账号已激活, 请前往登录页面登录！");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "激活失败！");
        }
        return resultMap;
    }
}
