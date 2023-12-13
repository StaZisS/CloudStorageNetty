package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.entity.ResponseTypeEnum;
import org.example.json.ResponseBody;
import org.example.service.SessionManager;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

import java.util.Map;

public class CommandRouter extends SimpleChannelInboundHandler<ResponseBody> {
    private final Map<ResponseTypeEnum, CommandHandler> handlers;
    private final SessionManager sessionManager;

    public CommandRouter(Map<ResponseTypeEnum, CommandHandler> handlers, SessionManager sessionManager) {
        this.handlers = handlers;
        this.sessionManager = sessionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        var sessionId = SessionManager.getSessionId(ctx);
        sessionManager.createSession(sessionId);
    }

    /**
     * Define the endpoint and check the user's authorization if the endpoint requires it
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody msg) throws Exception {
        var type = JsonUtils.getTypeResponseEnum(msg.getTypeRequest());
        if (type == null) {
            ResponseUserUtils.sendError(ctx, "Unknown type: " + msg.getTypeRequest());
            return;
        }

        if (type.isAuthRequired()) {
            var sessionId = SessionManager.getSessionId(ctx);
            if (!sessionManager.isSessionAuthenticated(sessionId)) {
                ResponseUserUtils.sendError(ctx, "User is not authorized");
                return;
            }
        }

        var handler = handlers.get(type);

        handler.handle(msg.getBody(), ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        var sessionId = SessionManager.getSessionId(ctx);
        sessionManager.deleteSession(sessionId);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        var sessionId = SessionManager.getSessionId(ctx);
        sessionManager.deleteSession(sessionId);
    }
}
