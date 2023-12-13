package org.example.handler.auth;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.SessionManager;
import org.example.utils.ResponseUserUtils;

@CommandType(ResponseTypeEnum.LOGOUT)
public class LogoutHandler implements CommandHandler {
    private final SessionManager sessionManager;

    @Inject
    public LogoutHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            var sessionId = SessionManager.getSessionId(ctx);
            var isAuthorized = sessionManager.isSessionAuthenticated(sessionId);

            if (isAuthorized) {
                sessionManager.setSessionAuthenticated(sessionId, false);
                ResponseUserUtils.sendOk(ctx, "You are logged out");
            } else {
                ResponseUserUtils.sendError(ctx, "You are not authorized");
            }
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.getMessage());
        }
    }

}
