package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.concurrent.GenericFutureListener;
import javafx.application.Platform;
import org.example.handler.FileHandler;
import org.example.handler.JsonHandler;
import org.example.json.RequestBody;
import org.example.json.ResponseBody;
import org.example.model.Callback;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class Network {
    private static final String ADDRESS = "localhost";
    private static final int PORT = 8189;
    private static final int MAX_CHUNK_SIZE = 64 * 1024;

    private static SocketChannel channel;

    private Network() {
    }

    public static void openConnection(Callback callback) {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ClientInitializer());
                var future = b.connect(ADDRESS, PORT).sync();
                channel = (SocketChannel) future.channel();
                Platform.runLater(callback::onSuccess);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                Platform.runLater(() -> callback.onFailure("Connection failed"));
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public static CompletableFuture<ResponseBody> sendMessageAndGetFuture(RequestBody msg) {
        CompletableFuture<ResponseBody> future = new CompletableFuture<>();
        var handler = new JsonHandler(future, channel);
        channel.pipeline().addLast(handler);
        channel.writeAndFlush(msg);
        return future;
    }

    public static void closeConnection() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
    }

    public static CompletableFuture<ChannelFuture> sendFile(File file) {
        CompletableFuture<ChannelFuture> future = new CompletableFuture<>();
        try {
            ChunkedFile chunkedFile = new ChunkedFile(file, MAX_CHUNK_SIZE);
            channel.writeAndFlush(chunkedFile).addListener((GenericFutureListener<ChannelFuture>) future::complete);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public static FileHandler downloadFile(String PathInClient, long sizeFile) {
        var fileHandler = new FileHandler(PathInClient, sizeFile);
        channel.pipeline().addLast(fileHandler);
        return fileHandler;
    }

    public static void removeFileHandler(FileHandler fileHandler) {
        channel.pipeline().remove(fileHandler);
    }

}
