package com.ledgerfx.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 用户名
     */
    private String username;
    /**
     * 加密密码
     */
    private String password;
}
