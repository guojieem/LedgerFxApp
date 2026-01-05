package com.ledgerfx.handler;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

public class FxExceptionHandler {
    public static void install() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            showErrorDialog(e);
        });
    }

    private static void showErrorDialog(Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("程序启动失败");
            alert.setHeaderText("LedgerFxApp 无法启动");

            TextArea area = new TextArea(getStackTrace(e));
            area.setEditable(false);
            area.setWrapText(true);

            alert.getDialogPane().setExpandableContent(area);
            alert.showAndWait();
        });
    }

    private static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
