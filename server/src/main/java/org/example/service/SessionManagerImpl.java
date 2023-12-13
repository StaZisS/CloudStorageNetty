package org.example.service;

import com.google.inject.Inject;
import org.example.repository.SessionRepository;

public class SessionManagerImpl implements SessionManager {
    private final SessionRepository sessionRepository;

    @Inject
    public SessionManagerImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void createSession(String sessionId) {
        sessionRepository.createSession(sessionId);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

    @Override
    public void setSessionAuthenticated(String sessionId, boolean isAuthorized) {
        sessionRepository.getSession(sessionId).setAuthorized(isAuthorized);
    }

    @Override
    public void incrementLoginAttempts(String sessionId) {
        sessionRepository.getSession(sessionId).incrementLoginAttempts();
    }

    @Override
    public void resetLoginAttempts(String sessionId) {
        sessionRepository.getSession(sessionId).resetLoginAttempts();
    }

    @Override
    public boolean isLoginAttemptsExceeded(String sessionId) {
        return sessionRepository.getSession(sessionId).getLoginAttempts() >= 3;
    }

    @Override
    public boolean isSessionAuthenticated(String sessionId) {
        return sessionRepository.getSession(sessionId).isAuthorized();
    }
}
