package org.example.presenter;

import org.example.model.Callback;
import org.example.model.LoginModel;
import org.example.view.LoginView;

public class LoginPresenter {
    private final LoginModel model;
    private final LoginView view;

    public LoginPresenter(LoginModel model, LoginView view) {
        this.model = model;
        this.view = view;
    }

    public void handleUsernameChange(String username) {
        model.setUsername(username);
    }

    public void handlePasswordChange(String password) {
        model.setPassword(password);
    }

    public void handleLoginButtonClicked() {
        if (model.getUsername() == null || model.getPassword() == null) {
            view.displayError("Username or password is empty");
            return;
        }

        model.login(new Callback() {
            @Override
            public void onSuccess() {
                view.displayLoginSuccess();
            }

            @Override
            public void onFailure(Object errorMessage) {
                view.displayError(errorMessage);
            }
        });
    }
}
