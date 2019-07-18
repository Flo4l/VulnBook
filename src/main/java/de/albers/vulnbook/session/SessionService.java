package de.albers.vulnbook.session;

import de.albers.vulnbook.user.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class SessionService {

    private SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(User user) throws NoSuchAlgorithmException, SQLException {
        String key = generateSessionKey(user.getUsername());
        LocalDateTime expires = LocalDateTime.now().plusSeconds(Session.MAX_AGE);
        Session session = new Session(key, expires, user.getUserId());
        sessionRepository.createSesion(session);
        return session;
    }

    public boolean validateSession(String key) throws SQLException {
        if(sessionRepository.getSessionByKey(key) != null) {
            return true;
        }
        return false;
    }

    public void deleteSession(Session session) throws SQLException {
        sessionRepository.deleteSession(session);
    }

    public String getSessionKey(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public boolean checkUserHasSession(User user) throws SQLException {
        return sessionRepository.checkUserHasSession(user);
    }

    public Session getSessionByKey(String key) throws SQLException {
        return sessionRepository.getSessionByKey(key);
    }

    private String generateSessionKey(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(input.getBytes());
        byte[] hashbytes = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashbytes.length; i++) {
            if ((0xff & hashbytes[i]) < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(0xff & hashbytes[i]));
        }
        return sb.toString();
    }
}
