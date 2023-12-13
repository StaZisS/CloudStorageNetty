package org.example.view;

import javafx.scene.control.Alert;

public class OkDialog {
    public static void showOk(Object message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ok");
        alert.setHeaderText(null);
        alert.setContentText((String) message);
        alert.showAndWait();
    }
}
