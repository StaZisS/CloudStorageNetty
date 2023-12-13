package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;
import org.example.view.OkDialog;

import java.io.File;
import java.io.FileOutputStream;

/**
 * This handler is used to receive file from server
 */
public class FileHandler extends ChannelInboundHandlerAdapter {

    private final long fileSize;
    private final String path;
    private File outputFile;
    private FileOutputStream fileOutputStream;
    private long totalBytesReceived = 0;

    public FileHandler(String path, long fileSize) {
        this.fileSize = fileSize;
        this.path = path;
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
                Platform.runLater(() -> OkDialog.showOk("File received and saved: " + outputFile.getAbsolutePath()));
            }

            ReferenceCountUtil.release(chunk);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
