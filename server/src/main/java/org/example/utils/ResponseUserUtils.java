package org.example.utils;

import io.netty.channel.ChannelHandlerContext;
import org.example.entity.StatusCodeEnum;
import static org.example.json.RequestBody.RequestBodyBuilder;


public class ResponseUserUtils {
    public static void sendError(ChannelHandlerContext ctx, String errorMessage) {
        ctx.writeAndFlush(new  RequestBodyBuilder()
                .setStatusCode(StatusCodeEnum.ERROR)
                .setBody(errorMessage).build()
        );
    }

    public static void sendOk(ChannelHandlerContext ctx, Object message) {
        ctx.writeAndFlush(new RequestBodyBuilder()
                .setStatusCode(StatusCodeEnum.OK)
                .setBody(message).build()
        );
    }
}
