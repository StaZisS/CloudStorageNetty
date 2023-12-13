package org.example.utils;

import io.netty.channel.ChannelFuture;
import javafx.application.Platform;
import org.example.Network;
import org.example.entity.StatusCodeEnum;
import org.example.json.RequestBody;
import org.example.model.Callback;

import java.util.concurrent.CompletableFuture;

public class CallbackHandlersUtils {
    public static void handlerCallback(Callback callback, RequestBody requestBody) {
        Network.sendMessageAndGetFuture(requestBody)
                .thenAcceptAsync(serverResult -> Platform.runLater(() -> {
                    var isSuccessful = serverResult.getStatusCode() == StatusCodeEnum.OK;
                    if (isSuccessful) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(serverResult.getBody());
                    }
                }))
                .exceptionally(throwable -> {
                    Platform.runLater(() -> callback.onFailure("Server is not available"));
                    return null;
                });
    }

    public static void sendFileCallback(Callback callback, CompletableFuture<ChannelFuture> future) {
        future.thenAcceptAsync(serverResult -> Platform.runLater(() -> {
            var isSuccessful = serverResult.isSuccess();
            if (isSuccessful) {
                callback.onSuccess();
            } else {
                callback.onFailure(serverResult.cause().getMessage());
            }
        })).exceptionally(throwable -> {
            Platform.runLater(() -> callback.onFailure("Server is not available"));
            return null;
        });
    }
}
