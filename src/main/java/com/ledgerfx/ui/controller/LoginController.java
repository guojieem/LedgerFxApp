package com.ledgerfx.ui.controller;

import com.ledgerfx.ui.enums.FxmlView;
import com.ledgerfx.ui.base.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LoginController extends BaseController {

    @FXML
    public Button loginBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void onShow() {
        usernameField.requestFocus();
    }

    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        // ====== 阶段 1：占位校验 ======
        if (username.isBlank()) {
            warn("请输入用户名");
            return;
        }

        // ====== 阶段 2：未来可替换为真实认证 ======

        // 登录成功 → 主界面
        switchView(FxmlView.DASHBOARD);
    }
}
