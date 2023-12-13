package org.example.model;

import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.utils.CallbackHandlersUtils;
import org.example.view.ErrorDialog;
import org.example.view.OkDialog;

public class LogoutModel {
    private LogoutModel() {
    }

    public static void logout() {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.LOGOUT)
                .build();

        CallbackHandlersUtils.handlerCallback(
                new Callback() {
                    @Override
                    public void onSuccess() {
                        OkDialog.showOk("Logout success");
                    }

                    @Override
                    public void onFailure(Object errorMessage) {
                        ErrorDialog.showError(errorMessage);
                    }
                }
                , requestBody);
    }
}
