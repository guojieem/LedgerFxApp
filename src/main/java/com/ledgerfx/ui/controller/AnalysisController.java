package com.ledgerfx.ui.controller;

import com.ledgerfx.entity.Bill;
import com.ledgerfx.service.BillService;
import com.ledgerfx.ui.base.BaseController;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

@Component
public class AnalysisController extends BaseController {

    @FXML
    private TableView<Bill> table;

    @Resource
    private BillService billService;

    public void initialize() {
        table.getItems().setAll(billService.all());
    }
}
