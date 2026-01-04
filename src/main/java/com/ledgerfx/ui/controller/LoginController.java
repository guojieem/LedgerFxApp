package com.ledgerfx.ui.controller;

import com.ledgerfx.service.UserService;
import com.ledgerfx.ui.StageManager;
import com.ledgerfx.ui.enums.FxmlView;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Resource
    private UserService userService;

    @FXML
    public void handleLogin() {
        boolean success = userService.login(usernameField.getText(), passwordField.getText());
        if (success) {
            StageManager.switchScene(FxmlView.BILL);
        } else {
            StageManager.showWarning("用户名或密码错误");
        }
    }
}

