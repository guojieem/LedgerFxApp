package com.ledgerfx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ledgerfx.context.UserContext;
import com.ledgerfx.domain.User;
import com.ledgerfx.mapper.UserMapper;
import com.ledgerfx.service.UserService;
import com.ledgerfx.util.PasswordEncoder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserContext userContext;

    @Override
    public boolean login(String username, String password) {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
        qw.eq(User::getUsername, username);

        User user = baseMapper.selectOne(qw);
        if (null == user) {
            return false;
        }
        boolean verifyPassword = PasswordEncoder.verifyPassword(password, user.getPassword(), user.getSalt());
        if (!verifyPassword) {
            return false;
        }
        userContext.setCurrentUser(user);
        return true;
    }

    @Override
    public boolean register(String username, String password) {
        if (existsByUsername(username)) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        String salt = PasswordEncoder.generateSalt();
        user.setSalt(salt);
        String encodedPassword = PasswordEncoder.encodePassword(password, salt);
        user.setPassword(encodedPassword);

        return baseMapper.insert(user) > 0;
    }

    public boolean existsByUsername(String username) {
        return baseMapper.selectCount(new QueryWrapper<User>().eq("username", username)) > 0;
    }

}
