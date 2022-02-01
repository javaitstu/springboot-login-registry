package com.study.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.study.mapper.UserMapper;
import com.study.pojo.User;
import com.study.service.MailService;
import com.study.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MailService mailService;

    /**
     * 创建账户
     *
     * @param user 管理用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> createAccount(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        String email = user.getEmail();
        // 1. 生成确认码
        String confirmCode  = IdUtil.getSnowflake(1,1).nextIdStr();
        // 2. 生成盐
        String salt = RandomUtil.randomString(6);
        // 3. 使用md5加密
        String md5Pwd = SecureUtil.md5(salt + user.getPassword());
        // 4. 生成失效时间
        LocalDateTime activationTime = LocalDateTime.now().plusDays(1);
        // 5. 初始化AdminUser
        user.setActivationTime(activationTime);
        user.setSalt(salt);
        user.setConfirmCode(confirmCode);
        user.setPassword(md5Pwd);
        user.setIsValid((byte) 0);
        List<User> users = userMapper.selectByEmail(email);
        if (users.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号已存在, 请前往登录页面！");
            return resultMap;
        }
        int result = userMapper.insertAdminUser(user);
        if (result > 0) {
            // 发送邮件
            String activationUrl = "http://localhost:8080/user/activation?confirmCode=" + confirmCode;
            System.out.println(email);
            mailService.sendMailForActivationAccount(activationUrl, email);
            resultMap.put("code", 200);
            resultMap.put("message", "注册成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "注册失败");
        }
        return resultMap;
    }

    /**
     * 激活账户
     *
     * @param confirmCode 确认代码
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new HashMap<>();
        User user = userMapper.selectActivationTimeByConfirmCode(confirmCode);
        boolean after = LocalDateTime.now().isAfter(user.getActivationTime());
        if (after) {
            resultMap.put("code", 400);
            resultMap.put("message", "链接超时,激活失败");
            return resultMap;
        }

        int result = userMapper.updateIsValidByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "激活成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "激活失败");
        }
        return resultMap;
    }

    /**
     * 登录账户
     *
     * @param user 用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> loginAccount(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> users = userMapper.selectPasswordByEmail(user.getEmail());
        if (users == null || users.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号不存在或未激活");
            return resultMap;
        }
        if (users.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号异常, 请联系管理员进行处理");
            return resultMap;
        }
        User u = users.get(0);
        String salt = u.getSalt();
        String md5Pwd = SecureUtil.md5(salt + user.getPassword());
        if (!md5Pwd.equals(u.getPassword())) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号或密码错误, 请重新输入");
            return resultMap;
        }

        resultMap.put("code", 200);
        resultMap.put("message", "登陆成功");
        return resultMap;
    }
}
