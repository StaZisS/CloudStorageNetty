package org.example.handler.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.example.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Handler for receive file
 */
public class FileServerHandler extends ChannelInboundHandlerAdapter {
    private static final String FILE_PATH = AppConfig.getProperty("server.file.dir");
    private final long fileSize;
    private final String path;
    private File outputFile;
    private FileOutputStream fileOutputStream;
    private long totalBytesReceived = 0;

    public FileServerHandler(String path, long fileSize) {
        this.fileSize = fileSize;
        this.path = FILE_PATH + path;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf chunk) {
            int readableBytes = chunk.readableBytes();
            totalBytesReceived += readableBytes;

            if (outputFile == null) {
                outputFile = new File(path);
                fileOutputStream = new FileOutputStream(outputFile);
            }

            byte[] chunkBytes = new byte[readableBytes];
            chunk.readBytes(chunkBytes);
            fileOutputStream.write(chunkBytes);

            if (totalBytesReceived == fileSize) {
                fileOutputStream.close();
                ctx.pipeline().remove(this);
            }

            ReferenceCountUtil.release(chunk);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

