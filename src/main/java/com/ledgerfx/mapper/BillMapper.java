package com.ledgerfx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ledgerfx.domain.Bill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {

    default List<Bill> findByUserId(Long userId) {
        return selectList(Wrappers.<Bill>lambdaQuery().eq(Bill::getUserId, userId));
    }
}