package com.ledgerfx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ledgerfx.domain.User;

public interface UserService extends IService<User> {

    /**
     * 登录验证
     */
    boolean login(String username, String password);

    boolean register(String username, String password);

}
