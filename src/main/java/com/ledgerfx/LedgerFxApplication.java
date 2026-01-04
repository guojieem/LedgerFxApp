package com.ledgerfx;

import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.enums.FxmlView;
import javafx.application.Application;
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
        StageManager.init(stage, context);
        StageManager.switchScene(FxmlView.LOGIN);
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

