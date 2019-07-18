package de.albers.vulnbook.session;

import java.time.LocalDateTime;

public class Session {

    public static final int MAX_AGE = 60 * 60 * 24 * 7;

    private long sessionId;
    private String key;
    private LocalDateTime expires;
    private long userId;

    public Session(String key, LocalDateTime expires, long userId) {
        this.key = key;
        this.expires = expires;
        this.userId = userId;
    }

    public Session(long sessionId, String key, LocalDateTime expires, long userId) {
        this(key, expires, userId);
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", key='" + key + '\'' +
                ", expires=" + expires +
                ", userId=" + userId +
                '}';
    }
}
