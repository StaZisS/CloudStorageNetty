package org.example.model;

import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.utils.CallbackHandlersUtils;

public class RegistrationModel {
    private String username;
    private String password;

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

    public void register(Callback callback) {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.REGISTER)
                .withBody(this)
                .build();

        CallbackHandlersUtils.handlerCallback(callback, requestBody);
    }
}
