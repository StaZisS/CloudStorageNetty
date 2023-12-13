package org.example.pageController;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.model.LoginModel;
import org.example.model.LogoutModel;
import org.example.presenter.LoginPresenter;
import org.example.view.ErrorDialog;
import org.example.view.LoginView;
import org.example.view.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable, LoginView {

    private final StageManager stageManager;
    public PasswordField passwordField;
    public TextField usernameField;
    private LoginPresenter presenter;

    @Inject
    LoginPageController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginModel model = new LoginModel();
        presenter = new LoginPresenter(model, this);
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
    public void displayLoginSuccess() {
        stageManager.switchScene(PagePath.MAIN);
    }

    @Override
    public void displayError(Object message) {
        ErrorDialog.showError(message);
    }

    public void loginUser(ActionEvent actionEvent) {
        setUsername(usernameField.getText());
        setPassword(passwordField.getText());
        presenter.handleLoginButtonClicked();
    }

    public void switchToRegistration(ActionEvent actionEvent) {
        stageManager.switchScene(PagePath.REGISTRATION);
    }

    public void logout(ActionEvent actionEvent) {
        LogoutModel.logout();
    }

    public void switchToMainPage(ActionEvent actionEvent) {
        stageManager.switchScene(PagePath.MAIN);
    }
}
