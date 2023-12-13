package org.example.pageController;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.example.Network;
import org.example.model.Callback;
import org.example.view.ErrorDialog;
import org.example.view.StageManager;


public class ConnectPageController implements Initializable {
    private final StageManager stageManager;

    @FXML
    Button connectionButton;

    @Inject
    ConnectPageController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    }

    public void connect(ActionEvent actionEvent) {
        Network.openConnection(new Callback() {
            @Override
            public void onSuccess() {
                stageManager.switchScene(PagePath.REGISTRATION);
            }

            @Override
            public void onFailure(Object errorMessage) {
                ErrorDialog.showError(errorMessage);
            }
        });
    }
}
