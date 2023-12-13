package org.example.handler.auth;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.LoginDTO;
import org.example.entity.ResponseTypeEnum;
import org.example.handler.CommandHandler;
import org.example.handler.CommandType;
import org.example.service.AuthenticationService;
import org.example.service.SessionManager;
import org.example.utils.JsonUtils;
import org.example.utils.ResponseUserUtils;

@CommandType(ResponseTypeEnum.LOGIN)
public class LoginHandler implements CommandHandler {
    private final AuthenticationService authService;
    private final SessionManager sessionManager;

    @Inject
    public LoginHandler(AuthenticationService authenticationService, SessionManager sessionManager) {
        this.authService = authenticationService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void handle(Object body, ChannelHandlerContext ctx) {
        try {
            LoginDTO loginDTO = JsonUtils.fromJson(body, LoginDTO.class);
            var sessionId = SessionManager.getSessionId(ctx);
            var isLoginSuccess = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
            var isLoginAttemptsExceeded = sessionManager.isLoginAttemptsExceeded(sessionId);
            var isAuthenticated = sessionManager.isSessionAuthenticated(sessionId);

            if (isLoginAttemptsExceeded) {
                ResponseUserUtils.sendError(ctx, "Login attempts exceeded");
            } else if (isAuthenticated) {
                ResponseUserUtils.sendError(ctx, "Already logged in");
            } else if (isLoginSuccess) {
                sessionManager.setSessionAuthenticated(sessionId, true);
                sessionManager.resetLoginAttempts(sessionId);
                ResponseUserUtils.sendOk(ctx, "Login successful");
            } else {
                sessionManager.incrementLoginAttempts(sessionId);
                ResponseUserUtils.sendError(ctx, "Invalid username or password");
            }
        } catch (Exception e) {
            ResponseUserUtils.sendError(ctx, e.getMessage());
        }
    }
}
