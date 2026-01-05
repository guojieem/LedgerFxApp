package com.ledgerfx.ui.controller;

import com.ledgerfx.ui.base.BaseController;
import com.ledgerfx.ui.enums.FxmlView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Component;

@Component
public class SplashController extends BaseController {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    /**
     * 更新进度条百分比
     * @param progress 范围 0.0 - 1.0
     */
    public void update(double progress) {
        Platform.runLater(() -> {
            if (progressBar != null) progressBar.setProgress(progress);
            if (progressLabel != null) {
                int percent = (int)(progress * 100);
                progressLabel.setText("Loading... " + percent + "%");
            }
        });
    }


}
