package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class JsonEncoder extends MessageToByteEncoder<RequestBody> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestBody msg, ByteBuf out) throws Exception {
        String json = objectMapper.writeValueAsString(msg);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_16);
        out.writeBytes(bytes);
    }
}
