package com.ledgerfx.ui.base;

import com.ledgerfx.ui.enums.FxmlView;
import com.ledgerfx.ui.StageManager;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class BaseController {

    /**
     * FXML 加载完成后调用
     * （等价于 initialize，但更干净）
     */
    public void onInit() {
    }

    /**
     * 窗口显示前调用
     */
    public void onShow() {
    }

    /* =======================
       Stage / Scene
       ======================= */
    protected Stage getStage() {
        return StageManager.getPrimaryStage();
    }

    protected Scene getScene() {
        return getStage().getScene();
    }

    protected Window getWindow() {
        return getStage();
    }

    /* =======================
       页面切换
       ======================= */
    protected void switchView(FxmlView view) {
        StageManager.switchScene(view);
    }

    protected void openWindow(FxmlView view) {
        StageManager.show(view);
    }

    protected void openModal(FxmlView view) {
        StageManager.showModal(view);
    }

    /* =======================
       Alert 快捷方法
       ======================= */
    protected void info(String message) {
        StageManager.showInfo(message);
    }

    protected void warn(String message) {
        StageManager.showWarning(message);
    }

    protected void error(String message) {
        StageManager.showError(message);
    }

    protected boolean confirm(String message) {
        return StageManager.showConfirm(message);
    }

    protected void close() {
        StageManager.getPrimaryStage().close();
    }
}
