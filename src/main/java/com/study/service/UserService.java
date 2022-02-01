package com.study.service;


import com.study.pojo.User;

import java.util.Map;

public interface UserService {

    // 账号注册
    Map<String, Object> createAccount(User user);

    Map<String, Object> activationAccount(String confirmCode);

    Map<String, Object> loginAccount(User user);
}
