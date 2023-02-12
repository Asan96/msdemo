package com.ydjk.msdemo.service;

import com.github.yulichang.base.MPJBaseService;
import com.ydjk.msdemo.entity.User;

import java.util.Map;

public interface UserService extends MPJBaseService<User> {
    String getCacheKey(Long uid);

    Map<String, Object> login(String username, String password);

    User addUser(User user);

    boolean logout(String token);

    User registerUser(User user);

    User getUser(String username);
}
