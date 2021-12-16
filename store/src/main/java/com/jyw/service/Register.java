package com.jyw.service;

import com.jyw.entity.User;

public interface Register {
    void insertUser(User user);
    User getUserByName(String username);
}
