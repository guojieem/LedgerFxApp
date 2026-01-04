package com.ledgerfx.service;

import com.ledgerfx.entity.Bill;
import com.ledgerfx.mapper.BillMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Resource
    private BillMapper billMapper;

    public void record(Bill bill) {
        billMapper.insert(bill);
    }

    public List<Bill> all() {
        return billMapper.selectList(null);
    }
}
