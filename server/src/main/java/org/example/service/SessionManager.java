package org.example.service;

import io.netty.channel.ChannelHandlerContext;

public interface SessionManager {
    static String getSessionId(ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }

    void createSession(String sessionId);

    void deleteSession(String sessionId);

    boolean isSessionAuthenticated(String sessionId);

    void setSessionAuthenticated(String sessionId, boolean isAuthorized);

    void incrementLoginAttempts(String sessionId);

    void resetLoginAttempts(String sessionId);

    boolean isLoginAttemptsExceeded(String sessionId);
}
