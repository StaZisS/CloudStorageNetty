package org.example.handler.file;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.SendFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.FileService;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

@CommandType(ResponseTypeEnum.SEND_FILE)
public class SendFileHandler implements CommandHandler {
    private final FileService fileService;

    @Inject
    public SendFileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        var sendFileDTO = JsonUtils.fromJson(body, SendFileDTO.class);

        if (fileService.isFileExist(sendFileDTO.getServerPath())) {
            ResponseUserUtils.sendError(ctx, "File already exist");
            return;
        }

        /*
        Add handler to pipeline for receive file
        */

        var handler = new FileServerHandler(sendFileDTO.getServerPath(), sendFileDTO.getFileSize());
        ctx.pipeline().addLast(handler);
        ResponseUserUtils.sendOk(ctx, "Allowed to transmit file");
    }
}
