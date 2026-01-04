package com.ledgerfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bills")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private BigDecimal amount;
    private LocalDateTime time;
    private String note;
}
