package org.example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.example.json.JsonDecoder;
import org.example.json.JsonEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private static final int MIN_BUFFER_SIZE = 64 * 1024;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        var channelConfig = socketChannel.config();
        channelConfig.setRecvByteBufAllocator(new FixedRecvByteBufAllocator(MIN_BUFFER_SIZE));
        socketChannel.pipeline().addLast(new JsonDecoder(), new JsonEncoder(), new ChunkedWriteHandler());
    }
}
