package com.jyw.service.impl;

import com.jyw.entity.User;
import com.jyw.entity.UserExample;
import com.jyw.mapper.UserMapper;
import com.jyw.service.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginImpl implements Login {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria=example.createCriteria();
        criteria.andUsernameEqualTo(username);
        return userMapper.selectByExample(example);
    }

    @Override
    public void insertUser(User user) {

    }
}
