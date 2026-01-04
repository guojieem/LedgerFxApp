package com.ledgerfx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ledgerfx.context.UserContext;
import com.ledgerfx.domain.User;
import com.ledgerfx.mapper.UserMapper;
import com.ledgerfx.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserContext userContext;

    @Override
    public boolean login(String username, String password) {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
        qw.eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .eq(User::getEnabled, true);

        User user = baseMapper.selectOne(qw);
        if (null == user) {
            return false;
        }
        userContext.setCurrentUser(user);
        return true;
    }

}
