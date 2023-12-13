package org.example.model.main_page;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import org.example.Network;
import org.example.dto.FileNodeDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.entity.StatusCodeEnum;
import org.example.json.RequestBodyBuilder;
import org.example.model.Callback;
import org.example.utils.JsonUtils;

public class MainModel {
    private FileNodeDTO fileNode;

    public FileNodeDTO getFileNode() {
        return fileNode;
    }

    public void requestFileTreeFromServer(Callback callback) {
        var requestBody = new RequestBodyBuilder()
                .withTypeRequest(ResponseTypeEnum.GET_FILE_TREE)
                .build();

        Network.sendMessageAndGetFuture(requestBody).thenAcceptAsync(
                serverResult -> Platform.runLater(() -> {
                    var isSuccessful = serverResult.getStatusCode() == StatusCodeEnum.OK;
                    if (isSuccessful) {
                        fileNode = JsonUtils.decodeJson(serverResult.getBody(), new TypeReference<FileNodeDTO>() {
                        });
                        callback.onSuccess();
                    } else {
                        callback.onFailure(serverResult.getBody());
                    }
                })
        ).exceptionally(throwable -> {
            Platform.runLater(() -> callback.onFailure("Server is not available"));
            return null;
        });
    }
}
