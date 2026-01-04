package com.ledgerfx.ui.controller;

import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.base.BaseController;
import com.ledgerfx.ui.enums.ContentView;
import com.ledgerfx.ui.enums.FxIcon;
import com.ledgerfx.ui.enums.FxmlView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class DashboardController extends BaseController {

    @FXML
    private StackPane contentPane;
    @FXML
    private Button homeBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button analysisBtn;
    @FXML
    private Button logoutBtn;

    @Override
    public void onInit() {
        // 默认显示首页
        loadContent(ContentView.HOME);
        // 给按钮绑定点击事件
        homeBtn.setGraphic(FxIcon.HOME.icon());
        homeBtn.setOnAction(e -> loadContent(ContentView.HOME));
        addBtn.setGraphic(FxIcon.ADD.icon());
        addBtn.setOnAction(e -> loadContent(ContentView.RECORD));
        analysisBtn.setGraphic(FxIcon.CHART.icon());
        analysisBtn.setOnAction(e -> loadContent(ContentView.ANALYSIS));
        logoutBtn.setGraphic(FxIcon.LOGOUT.icon());
    }

    /**
     * 加载内嵌视图到 contentPane
     */
    private void loadView(FxmlView view) {
        Parent node = StageManager.loadContent(view.getFxml());
        contentPane.getChildren().setAll(node);
    }

    /* =========================
       内容区切换（最终版）
       ========================= */
    private void loadContent(ContentView view) {
        Node content = StageManager.loadContent(view.getFxml());
        contentPane.getChildren().setAll(content);
    }

    @FXML
    private void handleHome() {
        loadContent(ContentView.HOME);
    }

    @FXML
    private void handleAddBill() {
        loadContent(ContentView.RECORD);
    }

    @FXML
    private void handleAnalysis() {
        loadContent(ContentView.ANALYSIS);
    }

    @FXML
    private void handleLogout() {
        if (confirm("确定要退出登录吗？")) {
            switchView(FxmlView.LOGIN);
        }
    }
}
