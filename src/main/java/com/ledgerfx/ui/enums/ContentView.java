package com.ledgerfx.ui.enums;

import lombok.Getter;

@Getter
public enum ContentView {

    HOME("/fxml/content/home.fxml"),
    RECORD("/fxml/content/record.fxml"),
    ANALYSIS("/fxml/content/analysis.fxml");

    private final String fxml;

    ContentView(String fxml) {
        this.fxml = fxml;
    }

}
