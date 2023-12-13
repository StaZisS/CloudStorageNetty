package org.example.model.main_page;

import org.example.Network;
import org.example.dto.SendFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.model.Callback;
import org.example.utils.CallbackHandlersUtils;
import org.example.view.ErrorDialog;
import org.example.view.OkDialog;

import java.io.File;

public class SendFileModel {

    public void sendFile(File file, String pathInServer, Callback sendFileCallback) {
        long fileSize = file.length();
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.SEND_FILE)
                .withBody(new SendFileDTO(pathInServer, fileSize))
                .build();

        CallbackHandlersUtils.handlerCallback(new Callback() {
            @Override
            public void onSuccess() {
                OkDialog.showOk("File started sending");
                CallbackHandlersUtils.sendFileCallback(sendFileCallback, Network.sendFile(file));
            }

            @Override
            public void onFailure(Object errorMessage) {
                ErrorDialog.showError(errorMessage);
            }
        }, requestBody);
    }
}
