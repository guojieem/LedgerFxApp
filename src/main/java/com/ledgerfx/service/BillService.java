package com.ledgerfx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ledgerfx.domain.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BillService extends IService<Bill> {

    boolean addBill(Bill bill);

    List<Bill> getBillsByUser(Long userId);

    // 新增方法：按分类统计金额
    Map<String, BigDecimal> sumByCategory(Long userId);

    // 新增方法：按日期统计金额
    Map<LocalDate, BigDecimal> sumByDate(Long userId);

}
