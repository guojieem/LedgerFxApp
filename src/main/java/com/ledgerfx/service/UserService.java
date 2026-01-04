package com.ledgerfx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ledgerfx.entity.User;
import com.ledgerfx.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User findByName(String name) {
        return userMapper.selectOne(
                new QueryWrapper<User>().eq("username", name)
        );
    }
}

