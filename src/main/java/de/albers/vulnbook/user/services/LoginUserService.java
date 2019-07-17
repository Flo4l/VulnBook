package de.albers.vulnbook.user.services;

import de.albers.vulnbook.session.Session;
import de.albers.vulnbook.session.SessionService;
import de.albers.vulnbook.user.User;
import de.albers.vulnbook.user.UserRepository;
import de.albers.vulnbook.user.exceptions.FieldEmptyException;
import de.albers.vulnbook.user.exceptions.IncorrectLoginException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void checkLoggedIn(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String key = getSessionKey(request);
        if (key == null || !sessionService.validateSession(key)) {
            response.sendRedirect("/login");
        }
    }

    public void checkFieldEmpty(User user) throws FieldEmptyException {
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            throw new FieldEmptyException();
        }
    }

    public void checkCorrectLogin(User user) throws IncorrectLoginException, SQLException {
        User queriedUser = userRepository.getUserByUsername(user.getUsername());
        if (queriedUser == null || !user.getPassword().equals(queriedUser.getPassword())) {
            throw new IncorrectLoginException();
        }
    }

    private Cookie bakeSessionCookie(User user) throws NoSuchAlgorithmException, SQLException {
        Session session = sessionService.createSession(user);
        Cookie cookie = new Cookie("session", session.getKey());
        cookie.setPath("/");
        cookie.setMaxAge(Session.MAX_AGE);
        return cookie;
    }

    private String getSessionKey(HttpServletRequest request) {
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
}
