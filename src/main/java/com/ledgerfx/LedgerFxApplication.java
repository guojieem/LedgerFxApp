package com.ledgerfx;

import com.ledgerfx.config.CustomSplashScreen;
import com.ledgerfx.views.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class LedgerFxApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(LedgerFxApplication.class, LoginView.class, new CustomSplashScreen(), args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(LedgerFxApplication.getStage());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}

