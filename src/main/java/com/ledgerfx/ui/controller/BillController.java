package com.ledgerfx.ui.controller;

import com.ledgerfx.context.UserContext;
import com.ledgerfx.domain.Bill;
import com.ledgerfx.domain.User;
import com.ledgerfx.service.BillService;
import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.enums.FxmlView;
import jakarta.annotation.Resource;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BillController {

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextField noteField;

    @FXML
    private TableView<Bill> billTable;
    @FXML
    private TableColumn<Bill, String> categoryCol;
    @FXML
    private TableColumn<Bill, BigDecimal> amountCol;
    @FXML
    private TableColumn<Bill, String> noteCol;
    @FXML
    private TableColumn<Bill, String> timeCol;
    @FXML
    private PieChart categoryChart;

    @Resource
    private BillService billService;
    @Resource
    private UserContext userContext;

    private User currentUser;

    @FXML
    public void initialize() {
        // 登录后注入当前用户
        this.currentUser = userContext.getCurrentUser();
        loadBills();
        loadCategoryCombo();

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        timeCol.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));

        billTable.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (!row.isEmpty()) {
                    highlightPieChart(row.getItem().getCategory());
                }
            });
            return row;
        });

    }

    @FXML
    public void handleExit() {
        // 弹出确认框
        if(StageManager.showConfirm("您确定要退出应用吗？")){
            // 清理全局用户信息
            userContext.clear();
            StageManager.getPrimaryStage().close();
        }
    }

    private void loadCategoryCombo() {
        Set<String> categories = new LinkedHashSet<>();
        categories.add("现金");
        categories.add("微信");
        categories.add("支付宝");
        categories.add("信用卡");
        categories.add("银行卡");
        categoryComboBox.getItems().setAll(categories);
    }

    private void highlightPieChart(String category) {
        for (PieChart.Data data : categoryChart.getData()) {
            data.getNode().setOpacity(data.getName().equals(category) ? 1.0 : 0.3);
        }
    }

    @FXML
    public void handleAddBill() {
        String category = categoryComboBox.getValue();
        String amountStr = amountField.getText();
        String note = noteField.getText();

        if (category.isEmpty() || amountStr.isEmpty()) {
            StageManager.showError("分类和金额不能为空");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);

            Bill bill = new Bill();
            bill.setUserId(currentUser.getId());
            bill.setCategory(category);
            bill.setAmount(amount);
            bill.setNote(note);
            bill.setCreateTime(LocalDateTime.now());

            if (billService.addBill(bill)) {
                StageManager.showInfo("账单添加成功");
                loadBills();
                amountField.clear();
                noteField.clear();
            } else {
                StageManager.showError("账单添加失败");
            }
        } catch (NumberFormatException e) {
            StageManager.showError("金额格式不正确");
        }
    }

    @FXML
    public void handleGoAnalysis() throws IOException {
        StageManager.switchScene(FxmlView.ANALYSIS);
    }

    @FXML
    public void handleExportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出账单");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(StageManager.getPrimaryStage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("分类,金额,备注,时间\n");
                for (Bill b : billService.getBillsByUser(currentUser.getId())) {
                    writer.write(String.format("%s,%s,%s,%s\n",
                            b.getCategory(),
                            b.getAmount(),
                            b.getNote(),
                            b.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    ));
                }
                StageManager.showInfo("账单已导出");
            } catch (IOException e) {
                StageManager.showError("导出失败");
            }
        }
    }

    private void loadBills() {
        List<Bill> bills = billService.getBillsByUser(currentUser.getId());
        billTable.getItems().setAll(bills);

        // 更新饼图
        Map<String, BigDecimal> map = billService.sumByCategory(currentUser.getId());
        javafx.collections.ObservableList<PieChart.Data> pieData = javafx.collections.FXCollections.observableArrayList();
        map.forEach((k, v) -> pieData.add(new PieChart.Data(k, v.doubleValue())));
        categoryChart.setData(pieData);
    }

}