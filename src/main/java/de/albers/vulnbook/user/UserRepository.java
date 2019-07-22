package de.albers.vulnbook.user;

import de.albers.vulnbook.DatabaseService;
import de.albers.vulnbook.session.Session;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO User (username, password, email) VALUES (?,?,?)";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.execute();
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM USER WHERE username = ?";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        }
    }

    public User getUserById(long userId) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM USER WHERE userid = " + userId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM USER";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(getUser(rs));
            }
            return users;
        }
    }

    public User getUserByMail(String mail) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        }
    }

    public User getUserBySession(Session session) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM user NATURAL JOIN session WHERE sessionid = " + session.getSessionId();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("userid");
        String username = resultSet.getString("username");
        String pass = resultSet.getString("password");
        String mail = resultSet.getString("email");
        return new User(id, username, pass, mail);
    }
}
