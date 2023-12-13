package org.example.handler;

import io.netty.channel.ChannelHandlerContext;

public interface CommandHandler {
    void handle(Object body, ChannelHandlerContext ctx);
}
