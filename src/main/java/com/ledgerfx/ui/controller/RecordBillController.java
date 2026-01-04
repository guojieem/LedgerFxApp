package com.ledgerfx.ui.controller;

import com.ledgerfx.entity.Bill;
import com.ledgerfx.service.BillService;
import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.enums.FxmlView;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class RecordBillController {
    @FXML
    private TextField tfAmount, tfCategory;
    @FXML
    private Button btnSave;

    @Resource
    private BillService billService;

    public void initialize() {
        btnSave.setOnAction(e -> {
            Bill bill = new Bill();
            bill.setAmount(new BigDecimal(tfAmount.getText()));
            bill.setCategory(tfCategory.getText());
            bill.setTime(LocalDateTime.now());
            billService.record(bill);
            StageManager.switchScene(FxmlView.DASHBOARD);
        });
    }
}

