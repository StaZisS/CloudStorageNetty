package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.example.json.ResponseBody;

import java.util.concurrent.CompletableFuture;

/**
 * Waiting for a response from the server
 */
public class JsonHandler extends SimpleChannelInboundHandler<ResponseBody> {
    private final CompletableFuture<ResponseBody> future;
    private final SocketChannel channel;

    public JsonHandler(CompletableFuture<ResponseBody> future, SocketChannel channel) {
        this.future = future;
        this.channel = channel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody msg) throws Exception {
        try {
            future.complete(msg);
        } finally {
            ReferenceCountUtil.release(msg);
            channel.pipeline().remove(this);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        future.completeExceptionally(cause);
        ctx.close();
    }
}
