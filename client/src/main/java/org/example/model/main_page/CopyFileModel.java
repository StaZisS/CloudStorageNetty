package org.example.model.main_page;

import org.example.dto.CopyFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.model.Callback;
import org.example.utils.CallbackHandlersUtils;
import org.example.view.ErrorDialog;
import org.example.view.OkDialog;

public class CopyFileModel {
    public void copyFile(String from, String to) {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.COPY_FILE)
                .withBody(new CopyFileDTO(from, to))
                .build();

        CallbackHandlersUtils.handlerCallback(new Callback() {
            @Override
            public void onSuccess() {
                OkDialog.showOk("File success copied");
            }

            @Override
            public void onFailure(Object errorMessage) {
                ErrorDialog.showError(errorMessage.toString());
            }
        }, requestBody);
    }
}
