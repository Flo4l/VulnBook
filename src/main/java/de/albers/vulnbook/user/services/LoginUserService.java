package de.albers.vulnbook.user.services;

import de.albers.vulnbook.session.Session;
import de.albers.vulnbook.session.SessionService;
import de.albers.vulnbook.user.User;
import de.albers.vulnbook.user.UserRepository;
import de.albers.vulnbook.user.exceptions.FieldEmptyException;
import de.albers.vulnbook.user.exceptions.IncorrectLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Service
public class LoginUserService {

    private UserRepository userRepository;
    private SessionService sessionService;

    @Autowired
    public LoginUserService(UserRepository userRepository, SessionService sessionService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    public void loginUser(User user, HttpServletResponse response) throws NoSuchAlgorithmException, IOException, SQLException {
        user = userRepository.getUserByUsername(user.getUsername());
        response.addCookie(bakeSessionCookie(user));
        response.sendRedirect("/feed");
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Session session = sessionService.getSessionByRequest(request);
        if (session != null) {
            sessionService.deleteSession(session);
            response.addCookie(endSessionCookie());
        }
    }

    public boolean checkLoggedIn(HttpServletRequest request) throws SQLException {
        String key = sessionService.getSessionKey(request);
        return sessionService.validateSession(key);
    }

    public void checkFieldEmpty(User user) throws FieldEmptyException {
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            throw new FieldEmptyException();
        }
    }

    public void checkCorrectLogin(User user) throws IncorrectLoginException, SQLException {
        User queriedUser = userRepository.getUserByUsername(user.getUsername());
        if (queriedUser == null || !new BCryptPasswordEncoder().matches(user.getPassword(), queriedUser.getPassword())) {
            throw new IncorrectLoginException();
        }
    }

    public User getLoggedInUser(HttpServletRequest request) throws SQLException {
        Session session = sessionService.getSessionByRequest(request);
        if (session != null) {
            return userRepository.getUserById(session.getUserId());
        }
        return null;
    }

    public User getUserById(long userId) throws SQLException {
        return userRepository.getUserById(userId);
    }

    private Cookie bakeSessionCookie(User user) throws NoSuchAlgorithmException, SQLException {
        Session session = sessionService.createSession(user);
        Cookie cookie = new Cookie("session", session.getKey());
        cookie.setPath("/");
        cookie.setMaxAge(Session.MAX_AGE);
        cookie.setSecure(true);
        return cookie;
    }

    private Cookie endSessionCookie() {
        Cookie cookie = new Cookie("session", "");
        cookie.setMaxAge(0);
        return cookie;
    }
}
