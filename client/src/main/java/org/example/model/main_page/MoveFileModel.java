package org.example.model.main_page;

import org.example.dto.MoveFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.model.Callback;
import org.example.utils.CallbackHandlersUtils;
import org.example.view.ErrorDialog;
import org.example.view.OkDialog;

public class MoveFileModel {
    public void moveFile(String from, String to) {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.MOVE_FILE)
                .withBody(new MoveFileDTO(from, to))
                .build();

        CallbackHandlersUtils.handlerCallback(new Callback() {
            @Override
            public void onSuccess() {
                OkDialog.showOk("File success moved");
            }

            @Override
            public void onFailure(Object errorMessage) {
                ErrorDialog.showError(errorMessage.toString());
            }
        }, requestBody);
    }
}
