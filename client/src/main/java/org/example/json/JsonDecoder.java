package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonDecoder extends ByteToMessageDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        String json = new String(bytes, StandardCharsets.UTF_16);
        try {
            ResponseBody response = objectMapper.readValue(json, ResponseBody.class);
            out.add(response);
        } catch (Exception e) {
            out.add(Unpooled.wrappedBuffer(bytes));
        }
    }
}
