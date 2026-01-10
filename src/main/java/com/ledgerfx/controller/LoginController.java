package com.ledgerfx.controller;

import com.ledgerfx.LedgerFxApplication;
import com.ledgerfx.service.UserService;
import com.ledgerfx.views.BillView;
import com.ledgerfx.views.RegisterView;
import de.felixroske.jfxsupport.FXMLController;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FXMLController
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Resource
    private UserService userService;

    @FXML
    protected void handleLogin() {
        boolean success = userService.login(usernameField.getText(), passwordField.getText());
        if (success) {
            LedgerFxApplication.showView(BillView.class);
        } else {
            LedgerFxApplication.showAlert(Alert.AlertType.ERROR, "错误", "用户名或密码错误");
        }
    }

    // ===================== 注册 =====================
    @FXML
    protected void handleRegister() {
        LedgerFxApplication.showView(RegisterView.class);
    }
}

