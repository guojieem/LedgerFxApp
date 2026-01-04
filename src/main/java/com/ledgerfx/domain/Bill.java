package com.ledgerfx.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_bill")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String currency;
    private String accountType;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime billTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}