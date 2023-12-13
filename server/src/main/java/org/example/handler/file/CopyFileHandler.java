package org.example.handler.file;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.CopyFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.FileService;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;


@CommandType(ResponseTypeEnum.COPY_FILE)
public class CopyFileHandler implements CommandHandler {
    private final FileService fileService;

    @Inject
    public CopyFileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            var copyFileDTO = JsonUtils.fromJson(body, CopyFileDTO.class);
            fileService.copyFile(copyFileDTO.getFrom(), copyFileDTO.getTo());
            ResponseUserUtils.sendOk(ctx, "File success copied");
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.toString());
        }

    }
}
