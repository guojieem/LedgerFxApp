package com.ledgerfx.ui.enums;

import lombok.Getter;

@Getter
public enum FxmlView {

    LOGIN("/fxml/Login.fxml", "登录", 400, 350),
    BILL("/fxml/Bill.fxml", "账单", 900, 600),
    ANALYSIS("/fxml/BillAnalysis.fxml", "账单分析", 900, 600);

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
