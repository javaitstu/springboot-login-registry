package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemController {

    /**
     * 登录页面跳转
     *
     * @return
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 注册页面跳转
     *
     * @return
     */
    @GetMapping("registry")
    public String registry() {
        return "registry";
    }
}
