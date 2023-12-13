package org.example.dto;

public class RegistrationDTO {
    private String username;
    private String password;

    public RegistrationDTO(String login, String password) {
        this.username = login;
        this.password = password;
    }

    public RegistrationDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
