package com.ledgerfx.context;

import com.ledgerfx.domain.User;
import org.springframework.stereotype.Component;

/**
 * 全局共享当前登录用户信息
 * 线程安全，整个应用只有一个当前用户
 */
@Component
public class UserContext {

    private User currentUser;

    /**
     * 获取当前登录用户
     */
    public synchronized User getCurrentUser() {
        return currentUser;
    }

    /**
     * 设置当前登录用户
     */
    public synchronized void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * 清空当前用户（登出时调用）
     */
    public synchronized void clear() {
        this.currentUser = null;
    }

    /**
     * 判断是否已登录
     */
    public synchronized boolean isLoggedIn() {
        return this.currentUser != null;
    }
}