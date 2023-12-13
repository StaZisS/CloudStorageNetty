package org.example.handler.file;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.MoveFileDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.FileService;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

@CommandType(ResponseTypeEnum.MOVE_FILE)
public class MoveFileHandler implements CommandHandler {
    private final FileService fileService;

    @Inject
    public MoveFileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            MoveFileDTO moveFileDTO = JsonUtils.fromJson(body, MoveFileDTO.class);
            fileService.moveFile(moveFileDTO.getFrom(), moveFileDTO.getTo());
            ResponseUserUtils.sendOk(ctx, "File success moved");
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.toString());
        }
    }
}
