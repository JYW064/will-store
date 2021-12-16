package com.jyw.service;

import com.jyw.entity.User;

public interface Login {
    User getUserByUsername(String username);

    void insertUser(User user);
}
