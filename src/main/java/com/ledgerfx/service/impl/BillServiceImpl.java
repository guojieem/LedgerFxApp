package com.ledgerfx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ledgerfx.domain.Bill;
import com.ledgerfx.mapper.BillMapper;
import com.ledgerfx.service.BillService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Override
    public boolean addBill(Bill bill) {
        return baseMapper.insert(bill) > 0;
    }

    @Override
    public List<Bill> getBillsByUser(Long userId) {
        return baseMapper.findByUserId(userId);
    }

    @Override
    public Map<String, BigDecimal> sumByCategory(Long userId) {
        List<Bill> bills = getBillsByUser(userId);
        return bills.stream()
                .collect(Collectors.groupingBy(
                        Bill::getAccountType,
                        Collectors.mapping(Bill::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    @Override
    public Map<LocalDate, BigDecimal> sumByDate(Long userId) {
        List<Bill> bills = getBillsByUser(userId);
        return bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getCreateTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.mapping(Bill::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

}
