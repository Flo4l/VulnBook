package de.albers.vulnbook.user.services;

import de.albers.vulnbook.user.User;
import de.albers.vulnbook.user.UserRepository;
import de.albers.vulnbook.user.exceptions.AlreadyRegisteredException;
import de.albers.vulnbook.user.exceptions.FieldEmptyException;
import de.albers.vulnbook.user.exceptions.UnequalPasswordsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RegisterUserService {

    private UserRepository userRepository;

    @Autowired
    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) throws SQLException {
        user.setPassword(encodePassword(user.getPassword()));
        userRepository.createUser(user);
    }

    public void checkPasswordEqual(String pass1, String pass2) {
        if(!pass1.equals(pass2)) {
            throw new UnequalPasswordsException();
        }
    }

    public void checkFieldEmpty(User user, String pass) {
        if(user.getUsername().equals("") || user.getEmail().equals("") || user.getPassword().equals("") || pass.equals("")) {
            throw new FieldEmptyException();
        }
    }

    public void checkAlreadyRegistered(User user) throws SQLException {
        if(userRepository.getUserByUsername(user.getUsername()) != null || userRepository.getUserByMail(user.getEmail()) != null) {
            throw new AlreadyRegisteredException("User already registered: " + user.getUsername());
        }
    }

    private String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
