package de.albers.vulnbook.user;

import de.albers.vulnbook.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public void createUser(User user) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO User (username, password, email) VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getEmail() +"')";
        stmt.execute(sql);
    }

    public User getUserByUsername(String username) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM USER WHERE username = " + username;
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return getUser(rs);
        }
        return null;
    }

    public User getUserById(long userId) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM USER WHERE userid = " + userId;
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return getUser(rs);
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM USER";
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<User> users = new ArrayList<>();
        while(rs.next()) {
            users.add(getUser(rs));
        }
        return users;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("userid");
        String username = resultSet.getString("username");
        String pass = resultSet.getString("password");
        String mail = resultSet.getString("email");
        return new User(id, username, pass, mail);
    }
}
