package com.ledgerfx.ui.controller;

import com.ledgerfx.service.UserService;
import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.enums.FxmlView;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @Resource
    private UserService userService;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            StageManager.showError("所有字段必须填写");
            return;
        }

        if (!password.equals(confirm)) {
            StageManager.showError("两次密码不一致");
            return;
        }

        // 调用 UserService 保存用户并初始化分类
        boolean flag = userService.register(username, password);

        if (flag) {
            StageManager.showInfo("注册成功，跳转登录");
            StageManager.switchScene(FxmlView.LOGIN);
        } else {
            StageManager.showWarning("用户名已存在");
        }
    }

    @FXML
    public void handleBackToLogin() {
        StageManager.switchScene(FxmlView.LOGIN);
    }
}
