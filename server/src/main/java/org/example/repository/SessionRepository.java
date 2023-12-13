package org.example.repository;

import org.example.entity.SessionEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRepository {
    private final Map<String, SessionEntity> sessions = new ConcurrentHashMap<>();

    public void createSession(String sessionId) {
        sessions.put(sessionId, new SessionEntity(sessionId));
    }

    public SessionEntity getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
