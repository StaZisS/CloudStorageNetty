package org.example.handler.file;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.config.AppConfig;
import org.example.dto.RequestDownloadFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.FileService;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

import java.io.File;

@CommandType(ResponseTypeEnum.DOWNLOAD_FILE)
public class DownloadFileHandler implements CommandHandler {
    private final static String ROOT_PATH = AppConfig.getProperty("server.file.dir");

    private final FileService fileService;

    @Inject
    public DownloadFileHandler(FileService sendService) {
        this.fileService = sendService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            RequestDownloadFileDTO requestDownloadFileDTO = JsonUtils.fromJson(body, RequestDownloadFileDTO.class);
            File file = new File(ROOT_PATH + requestDownloadFileDTO.getServerPath());
            if (!file.exists()) {
                ResponseUserUtils.sendError(ctx, "File not found");
                return;
            }

            if (file.isDirectory()) {
                ResponseUserUtils.sendError(ctx, "File is directory");
                return;
            }

            ResponseUserUtils.sendOk(ctx, "File start sending");
            fileService.sendFile(file, ctx.channel());
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.getMessage());
        }
    }
}
