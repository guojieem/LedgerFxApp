package com.ledgerfx.config;

import de.felixroske.jfxsupport.SplashScreen;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * 自定义闪屏动画
 */
public class CustomSplashScreen extends SplashScreen {

    /**
     * 是否显示闪屏动画
     */
    private static final Boolean isShowSplash = true;

    @Override
    public Parent getParent() {
        final ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource(getImagePath())).toExternalForm());

        final ProgressBar splashProgressBar = new ProgressBar();
        splashProgressBar.setPrefWidth(imageView.getImage().getWidth());

        Label startupLabel = new Label("正在启动, 请稍等...");
        startupLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;-fx-padding: 5 0;");

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(startupLabel);

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(imageView, hbox, splashProgressBar);

        return vbox;
    }

    @Override
    public String getImagePath() {
        return "/icons/logo.png";
    }

    @Override
    public boolean visible() {
        return isShowSplash;
    }

}
