package de.albers.vulnbook.session;

import de.albers.vulnbook.DatabaseService;
import de.albers.vulnbook.user.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

@Repository
public class SessionRepository {

    public void createSession(Session session) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "INSERT INTO session (`key`, expires, userid) VALUES ('" + session.getKey() + "', '" + session.getExpires() + "', " + session.getUserId() + ")";
            stmt.execute(sql);
        }
    }

    public Session getSessionByKey(String key) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM session WHERE `key` = '" + key + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return getSession(rs);
            }
            return null;
        }
    }

    public void deleteSession(Session session) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "DELETE FROM session WHERE `key` = '" + session.getKey() + "'";
            stmt.execute(sql);
        }
    }

    public boolean checkUserHasSession(User user) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM session WHERE userid = " + user.getUserId();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
            return false;
        }
    }

    private Session getSession(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("sessionid");
        String key = resultSet.getString("key");
        LocalDateTime expires = resultSet.getTimestamp("expires").toLocalDateTime();
        long userid = resultSet.getLong("userid");
        return new Session(id, key, expires, userid);
    }
}
