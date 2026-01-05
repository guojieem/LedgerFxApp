package com.ledgerfx;

import com.ledgerfx.handler.FxExceptionHandler;
import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.controller.SplashController;
import com.ledgerfx.ui.enums.FxmlView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LedgerFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(LedgerFxApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        FxExceptionHandler.install();
        // 初始化 StageManager（使用 Spring 容器）
        StageManager.init(stage, context);
        // 先显示 Splash
        StageManager.switchScene(FxmlView.SPLASH);
        // 模拟加载进度
        SplashController splash = context.getBean(SplashController.class);
        new Thread(() -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    Thread.sleep(15);  // 模拟初始化耗时
                    splash.update(i / 100.0);
                }
                // 完成后切换登录页面
                Platform.runLater(() -> StageManager.switchScene(FxmlView.LOGIN));
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

