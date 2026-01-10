package com.ledgerfx.controller;

import com.ledgerfx.LedgerFxApplication;
import com.ledgerfx.context.UserContext;
import com.ledgerfx.domain.Bill;
import com.ledgerfx.domain.User;
import com.ledgerfx.service.BillService;
import com.ledgerfx.views.BillView;
import de.felixroske.jfxsupport.FXMLController;
import jakarta.annotation.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@FXMLController
public class BillAnalysisController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private PieChart categoryChart;
    @FXML
    private LineChart<String, Number> trendChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @Resource
    private BillService billService;
    @Resource
    private UserContext userContext;

    private User currentUser;

    @FXML
    protected void initialize() {
        // 登录后注入当前用户
        this.currentUser = userContext.getCurrentUser();
        loadCharts();
        loadCategoryCombo();
    }

    @FXML
    protected void handleBackToBill() {
        LedgerFxApplication.showView(BillView.class);
    }

    private void loadCategoryCombo() {
        List<Bill> bills = billService.getBillsByUser(currentUser.getId());
        Set<String> categories = bills.stream().map(Bill::getAccountType).collect(Collectors.toSet());
        categoryComboBox.getItems().setAll(categories);
    }

    private void loadCharts() {
        loadCategoryChart();
        loadTrendChart();
    }

    private void loadCategoryChart() {
        Map<String, BigDecimal> map = billService.sumByCategory(currentUser.getId());
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        map.forEach((k, v) -> pieData.add(new PieChart.Data(k, v.doubleValue())));
        categoryChart.setData(pieData);
    }

    private void loadTrendChart() {
        Map<LocalDate, BigDecimal> map = billService.sumByDate(currentUser.getId());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        map.forEach((date, amount) -> series.getData().add(new XYChart.Data<>(date.toString(), amount.doubleValue())));
        trendChart.getData().setAll(series);
    }

    @FXML
    protected void handleFilter() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String category = categoryComboBox.getValue();

        List<Bill> bills = billService.getBillsByUser(currentUser.getId());
        List<Bill> filtered = bills.stream()
                .filter(b -> (start == null || !b.getCreateTime().toLocalDate().isBefore(start)) &&
                        (end == null || !b.getCreateTime().toLocalDate().isAfter(end)) &&
                        (category == null || category.equals(b.getAccountType())))
                .collect(Collectors.toList());

        // 更新饼图
        Map<String, BigDecimal> categoryMap = filtered.stream()
                .collect(Collectors.groupingBy(Bill::getAccountType,
                        Collectors.mapping(Bill::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        categoryMap.forEach((k, v) -> pieData.add(new PieChart.Data(k, v.doubleValue())));
        categoryChart.setData(pieData);

        // 更新折线图
        Map<LocalDate, BigDecimal> dateMap = filtered.stream()
                .collect(Collectors.groupingBy(b -> b.getCreateTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.mapping(Bill::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        dateMap.forEach((date, amount) -> series.getData().add(new XYChart.Data<>(date.toString(), amount.doubleValue())));
        trendChart.getData().setAll(series);
    }

    @FXML
    protected void handleExportChart(Pane chartPane) {
        WritableImage image = chartPane.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出图表");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
