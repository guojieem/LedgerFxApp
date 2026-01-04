package com.ledgerfx.ui.enums;

import lombok.Getter;

@Getter
public enum FxmlView {

    LOGIN("/fxml/login.fxml", "登录", 400, 350),
    DASHBOARD("/fxml/dashboard.fxml", "Dashboard", 900, 600),
    HOME("/fxml/home.fxml", "首页", 700, 500),
    RECORD_BILL("/fxml/recordBill.fxml", "记账", 700, 500),
    ANALYSIS("/fxml/analysis.fxml", "账单分析", 700, 500);

    private final String fxml;
    private final String title;
    private final double width;
    private final double height;

    FxmlView(String fxml, String title, double width, double height) {
        this.fxml = fxml;
        this.title = title;
        this.width = width;
        this.height = height;
    }

}
