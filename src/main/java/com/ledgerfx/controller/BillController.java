package com.ledgerfx.controller;

import com.ledgerfx.LedgerFxApplication;
import com.ledgerfx.context.UserContext;
import com.ledgerfx.domain.Bill;
import com.ledgerfx.domain.User;
import com.ledgerfx.service.BillService;
import com.ledgerfx.views.BillAnalysisView;
import de.felixroske.jfxsupport.FXMLController;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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

@Slf4j
@FXMLController
public class BillController {

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextField noteField;
    @FXML
    private TableView<Bill> billTable;
    @FXML
    private TableColumn<Bill, String> accountTypeCol;
    @FXML
    private TableColumn<Bill, BigDecimal> amountCol;
    @FXML
    private TableColumn<Bill, String> typeCol;
    @FXML
    private TableColumn<Bill, String> descriptionCol;
    @FXML
    private TableColumn<Bill, String> timeCol;
    @FXML
    private PieChart categoryChart;

    @Autowired
    private BillService billService;
    @Autowired
    private UserContext userContext;

    private User currentUser;

    @FXML
    protected void initialize() {
        // 登录后注入当前用户
        this.currentUser = userContext.getCurrentUser();
        loadBills();
        loadTypeComboBox();
        loadCategoryCombo();

        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        timeCol.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getBillTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        ));

        billTable.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (!row.isEmpty()) {
                    highlightPieChart(row.getItem().getAccountType());
                }
            });
            return row;
        });

    }

    @FXML
    protected void handleExit() {
        // 弹出确认框
        if (LedgerFxApplication.showConfirm("您确定要退出应用吗？")) {
            // 清理全局用户信息
            userContext.clear();
            Platform.exit();
        }
    }

    private void loadTypeComboBox() {
        Set<String> type = new LinkedHashSet<>();
        type.add("收入");
        type.add("支出");
        typeComboBox.getItems().setAll(type);
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
    protected void handleAddBill() {
        String category = categoryComboBox.getValue();
        String amountStr = amountField.getText();
        String typeStr = typeComboBox.getValue();
        String note = noteField.getText();

        if (!StringUtils.hasLength(category)) {
            LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "请选择分类");
            return;
        }
        if (!StringUtils.hasLength(amountStr)) {
            LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "请填入金额");
            return;
        }
        if (!StringUtils.hasLength(typeStr)) {
            LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "请选择类型");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);

            Bill bill = new Bill();
            bill.setUserId(currentUser.getId());
            bill.setAccountType(category);
            bill.setAmount(amount);
            bill.setType(typeStr);
            bill.setDescription(note);
            bill.setBillTime(LocalDateTime.now());
            bill.setCreateTime(LocalDateTime.now());

            if (billService.addBill(bill)) {
                LedgerFxApplication.showAlert(Alert.AlertType.INFORMATION, "提示", "账单添加成功");
                loadBills();
                amountField.clear();
                noteField.clear();
            } else {
                LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "账单添加失败");
            }
        } catch (NumberFormatException e) {
            LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "金额格式不正确");
        }
    }

    @FXML
    protected void handleGoAnalysis() throws IOException {
        LedgerFxApplication.showView(BillAnalysisView.class);
    }

    @FXML
    protected void handleExportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出账单");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(LedgerFxApplication.getStage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("分类,金额,类型,备注,时间\n");
                for (Bill b : billService.getBillsByUser(currentUser.getId())) {
                    writer.write(String.format("%s,%s,%s,%s,%s\n",
                            b.getAccountType(),
                            b.getAmount(),
                            b.getType(),
                            b.getDescription(),
                            b.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    ));
                }
                LedgerFxApplication.showAlert(Alert.AlertType.INFORMATION, "提示", "账单已导出");
            } catch (IOException e) {
                LedgerFxApplication.showAlert(Alert.AlertType.WARNING, "警告", "导出失败");
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