package com.ledgerfx.ui.controller;

import com.ledgerfx.service.UserService;
import com.ledgerfx.ui.base.BaseController;
import com.ledgerfx.ui.enums.FxmlView;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginController extends BaseController {

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
            switchView(FxmlView.BILL);
        } else {
            warn("用户名或密码错误");
        }
    }

    // ===================== 注册 =====================
    @FXML
    private void handleRegister() {
        switchView(FxmlView.REGISTER);
    }
}

