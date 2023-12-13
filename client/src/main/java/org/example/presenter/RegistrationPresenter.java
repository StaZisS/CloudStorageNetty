package org.example.presenter;

import org.example.model.Callback;
import org.example.model.RegistrationModel;
import org.example.view.RegistrationView;

public class RegistrationPresenter {
    private final RegistrationModel model;
    private final RegistrationView view;

    public RegistrationPresenter(RegistrationModel model, RegistrationView view) {
        this.model = model;
        this.view = view;
    }

    public void handleUsernameChange(String username) {
        if (username.length() > 20) {
            view.displayError("Username is too long, max 20 characters");
            return;
        }
        model.setUsername(username);
    }

    public void handlePasswordChange(String password) {
        if (password.length() > 20 || password.length() < 6) {
            view.displayError("Password is too long, max 20 characters, min 6 characters");
            return;
        }
        model.setPassword(password);
    }

    public void handleRegisterButtonClicked() {
        if (model.getUsername() == null || model.getPassword() == null) {
            view.displayError("Username or password is empty");
            return;
        }

        model.register(new Callback() {
            @Override
            public void onSuccess() {
                view.displayRegistrationSuccess();
            }

            @Override
            public void onFailure(Object errorMessage) {
                view.displayError(errorMessage);
            }
        });
    }
}
