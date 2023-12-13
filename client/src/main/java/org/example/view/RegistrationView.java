package org.example.view;

public interface RegistrationView {
    void setUsername(String username);

    void setPassword(String password);

    void displayRegistrationSuccess();

    void displayError(Object message);
}
