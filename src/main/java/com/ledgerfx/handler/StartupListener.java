package com.ledgerfx.handler;

import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.controller.SplashController;
import com.ledgerfx.ui.enums.FxmlView;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {
    private final SplashController splash;

    public StartupListener(SplashController splash) {
        this.splash = splash;
    }

    @EventListener
    public void onStarted(ApplicationReadyEvent event) {
        splash.update(1.0);
        StageManager.switchScene(FxmlView.LOGIN);
    }
}
