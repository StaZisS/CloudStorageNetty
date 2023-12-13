package org.example.view;

public interface LoginView {
    void setUsername(String username);

    void setPassword(String password);

    void displayLoginSuccess();

    void displayError(Object message);
}
