package com.jyw.service.impl;

import com.jyw.entity.User;
import com.jyw.entity.UserExample;
import com.jyw.mapper.UserMapper;
import com.jyw.service.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterImpl implements Register {
    @Autowired
    UserMapper userMapper;

    @Override
    public void insertUser(User user) {
       userMapper.insertSelective(user);
    }

    @Override
    public User getUserByName(String username) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria=example.createCriteria();
        criteria.andUsernameEqualTo(username);
        return userMapper.selectByExample(example);
    }
}
