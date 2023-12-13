package org.example.pageController;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.model.LogoutModel;
import org.example.model.RegistrationModel;
import org.example.presenter.RegistrationPresenter;
import org.example.view.ErrorDialog;
import org.example.view.RegistrationView;
import org.example.view.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationPageController implements Initializable, RegistrationView {
    private final StageManager stageManager;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    private RegistrationPresenter presenter;

    @Inject
    RegistrationPageController(StageManager stageManager) {
        this.stageManager = stageManager;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RegistrationModel model = new RegistrationModel();
        presenter = new RegistrationPresenter(model, this);
    }

    @Override
    public void setUsername(String username) {
        presenter.handleUsernameChange(username);
    }

    @Override
    public void setPassword(String password) {
        presenter.handlePasswordChange(password);
    }

    @Override
    public void displayRegistrationSuccess() {
        stageManager.switchScene(PagePath.LOGIN);
    }

    @Override
    public void displayError(Object message) {
        ErrorDialog.showError(message);
    }

    public void registerUser() {
        if (passwordField.getText().equals(confirmPasswordField.getText())) {
            setUsername(usernameField.getText());
            setPassword(passwordField.getText());
            presenter.handleRegisterButtonClicked();
        } else {
            displayError("Passwords do not match");
        }
    }

    public void switchToLogin() {
        stageManager.switchScene(PagePath.LOGIN);
    }

    public void logout(ActionEvent actionEvent) {
        LogoutModel.logout();
    }

    public void switchToMainPage(ActionEvent actionEvent) {
        stageManager.switchScene(PagePath.MAIN);
    }
}
