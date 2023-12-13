package org.example.model.main_page;

import org.example.Network;
import org.example.dto.RequestDownloadFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.model.Callback;
import org.example.utils.CallbackHandlersUtils;
import org.example.view.OkDialog;

public class ReceiveFileModel {
    public void receivedFile(String pathInServer, String PathInClient, long fileSize) {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.DOWNLOAD_FILE)
                .withBody(new RequestDownloadFileDTO(pathInServer))
                .build();

        var handler = Network.downloadFile(PathInClient, fileSize);

        CallbackHandlersUtils.handlerCallback(new Callback() {
            @Override
            public void onSuccess() {
                OkDialog.showOk("File started receiving");
            }

            @Override
            public void onFailure(Object errorMessage) {
                System.out.println("Error: " + errorMessage);
                Network.removeFileHandler(handler);
            }
        }, requestBody);
    }
}
