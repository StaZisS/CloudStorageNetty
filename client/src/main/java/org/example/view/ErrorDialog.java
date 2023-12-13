package org.example.view;

import javafx.scene.control.Alert;

public class ErrorDialog {
    public static void showError(Object errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText((String) errorMessage);
        alert.showAndWait();
    }
}
