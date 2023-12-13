package org.example;

import com.google.inject.Inject;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.example.config.AppConfig;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandRouter;
import org.example.handler.CommandType;
import org.example.json.JsonDecoder;
import org.example.json.JsonEncoder;
import org.example.service.SessionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Map<ResponseTypeEnum, CommandHandler> handlers;
    private final SessionManager sessionManager;

    /**
     * Mapping of the endpoint to the handler
     */
    @Inject
    public ServerInitializer(Set<CommandHandler> commandHandlers, SessionManager sessionManager) {
        this.handlers = commandHandlers.stream()
                .collect(Collectors.toMap(handler -> {
                    CommandType annotation = handler.getClass().getAnnotation(CommandType.class);
                    if (annotation != null) {
                        return annotation.value();
                    }
                    throw new IllegalArgumentException("Command handler is missing CommandType annotation");
                }, Function.identity()));
        this.sessionManager = sessionManager;
        try {
            createDirectoryForServerFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for server file");
        }
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new JsonDecoder(), new JsonEncoder());
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new CommandRouter(handlers, sessionManager));
    }

    private void createDirectoryForServerFile() throws IOException {
        Path path = Path.of(AppConfig.getProperty("server.file.dir"));
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }
}
