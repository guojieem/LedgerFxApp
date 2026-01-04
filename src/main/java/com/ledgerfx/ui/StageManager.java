package com.ledgerfx.ui;

import com.ledgerfx.ui.base.BaseController;
import com.ledgerfx.ui.enums.FxmlView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Component
public final class StageManager {

    @Getter
    private static Stage primaryStage;
    private static ApplicationContext applicationContext;

    /* =====================
       全局样式
       ===================== */

    private static final List<String> GLOBAL_STYLES = List.of("/css/style.css");

    private StageManager() {
    }

    /* =====================
       初始化
       ===================== */

    public static void init(Stage stage, ApplicationContext context) {
        primaryStage = stage;
        applicationContext = context;
        primaryStage.setResizable(false);
    }

    /* =====================
       首次显示主窗口（不切 Scene）
       ===================== */

    public static void show(FxmlView view) {
        try {
            FXMLLoader loader = createLoader(view.getFxml());
            Parent root = loader.load();

            Scene scene = new Scene(root, view.getWidth(), view.getHeight());
            applyGlobalStyles(scene);

            primaryStage.setTitle(view.getTitle());
            primaryStage.setScene(scene);

            autoCenter(primaryStage);
            primaryStage.show();

            callOnInit(loader.getController());

        } catch (IOException e) {
            throw new RuntimeException("Failed to show view: " + view, e);
        }
    }

    /* =====================
       主窗口 Scene 切换
       ===================== */

    public static void switchScene(FxmlView view) {
        show(view);
    }

    /* =====================
       模态 / 非模态窗口
       ===================== */

    public static void showModal(FxmlView view) {
        showDialog(view, true);
    }

    public static void showWindow(FxmlView view) {
        showDialog(view, false);
    }

    private static void showDialog(FxmlView view, boolean modal) {
        try {
            FXMLLoader loader = createLoader(view.getFxml());
            Parent root = loader.load();

            Scene scene = new Scene(root, view.getWidth(), view.getHeight());
            applyGlobalStyles(scene);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(view.getTitle());
            stage.setScene(scene);
            stage.initOwner(primaryStage);

            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            autoCenter(stage);
            stage.show();

            callOnInit(loader.getController());

        } catch (IOException e) {
            throw new RuntimeException("Failed to show dialog: " + view, e);
        }
    }

    /* =====================
       内容视图加载（内嵌）
       ===================== */

    public static Parent loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = createLoader(fxmlPath);
            Parent root = loader.load();

            callOnInit(loader.getController());

            return root;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load content: " + fxmlPath, e);
        }
    }

    /* =====================
       Alert
       ===================== */

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /* =====================
       内部工具方法
       ===================== */

    private static FXMLLoader createLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StageManager.class.getResource(fxml));
        loader.setControllerFactory(applicationContext::getBean);
        return loader;
    }

    private static void applyGlobalStyles(Scene scene) {
        for (String css : GLOBAL_STYLES) {
            URL url = StageManager.class.getResource(css);
            if (url != null) {
                scene.getStylesheets().add(url.toExternalForm());
            }
        }
    }

    private static void callOnInit(Object controller) {
        if (controller instanceof BaseController bc) {
            Platform.runLater(bc::onInit);
        }
    }

    private static void autoCenter(Stage stage) {
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    /* =======================
       Alert 封装
       ======================= */
    public static void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, "提示", message);
    }

    public static void showWarning(String message) {
        showAlert(Alert.AlertType.WARNING, "警告", message);
    }

    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "错误", message);
    }

    public static boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
