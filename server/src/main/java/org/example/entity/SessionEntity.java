package org.example.entity;

public class SessionEntity {
    private String sessionId;
    private int loginAttempts;
    private boolean isAuthorized;

    public SessionEntity(String sessionId) {
        this(sessionId, 0, false);
    }

    private SessionEntity(String sessionId, int loginAttempts, boolean isAuthorized) {
        this.sessionId = sessionId;
        this.loginAttempts = loginAttempts;
        this.isAuthorized = isAuthorized;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public void incrementLoginAttempts() {
        loginAttempts++;
    }

    public void resetLoginAttempts() {
        loginAttempts = 0;
    }
}
