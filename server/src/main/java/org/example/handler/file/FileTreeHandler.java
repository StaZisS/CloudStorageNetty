package org.example.handler.file;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.config.AppConfig;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.FileService;
import org.example.utils.ResponseUserUtils;

import java.io.IOException;

@CommandType(ResponseTypeEnum.GET_FILE_TREE)
public class FileTreeHandler implements CommandHandler {
    private final FileService fileService;
    private final String directoryPath = AppConfig.getProperty("server.file.dir");

    @Inject
    public FileTreeHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            var fileTree = fileService.getFileTree(directoryPath);
            ResponseUserUtils.sendOk(ctx, fileTree);
        } catch (IOException e) {
            ResponseUserUtils.sendError(ctx, e.getMessage());
        }
    }
}
