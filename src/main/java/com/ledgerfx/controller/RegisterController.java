package com.ledgerfx.controller;

import com.ledgerfx.LedgerFxApplication;
import com.ledgerfx.service.UserService;
import com.ledgerfx.views.LoginView;
import de.felixroske.jfxsupport.FXMLController;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FXMLController
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
    protected void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("所有字段必须填写");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("两次密码不一致");
            return;
        }

        // 调用 UserService 保存用户并初始化分类
        boolean flag = userService.register(username, password);

        if (flag) {
            errorLabel.setText("注册成功，跳转登录");
            LedgerFxApplication.showView(LoginView.class);
        } else {
            errorLabel.setText("用户名已存在");
        }
    }

    @FXML
    protected void handleBackToLogin() {
        LedgerFxApplication.showView(LoginView.class);
    }
}
