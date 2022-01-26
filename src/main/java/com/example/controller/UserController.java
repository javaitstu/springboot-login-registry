package com.example.controller;

import com.example.pojo.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 创建账户
     *
     * @param user 用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @PostMapping("create")
    public Map<String, Object> createAccount(User user){
        return userService.creatAccount(user);
    }

    /**
     * 登录账户
     *
     * @param user 用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @PostMapping("login")
    public Map<String, Object> loginAccount(User user) {
        return userService.loginAccount(user);
    }

    @GetMapping("activation")
    public Map<String, Object> activationAccount(String confirmCode) {
        return userService.activationAccount(confirmCode);
    }
}
