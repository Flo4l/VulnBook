package de.albers.vulnbook.session;

import de.albers.vulnbook.DatabaseService;
import de.albers.vulnbook.user.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class SessionRepository {

    public void createSession(Session session) throws SQLException {
        String sql = "INSERT INTO session (`key`, expires, userid) VALUES (?,?,?)";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, session.getKey());
            stmt.setTimestamp(2, Timestamp.valueOf(session.getExpires()));
            stmt.setLong(1, session.getUserId());
            stmt.execute(sql);
        }
    }

    public void deleteExpiredSessions() throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "CALL DeleteExpired();";
            stmt.execute(sql);
        }
    }

    public void deleteSessionsForUser(User user) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "DELETE FROM session WHERE userid = " + user.getUserId();
            stmt.execute(sql);
        }
    }

    public Session getSessionByKey(String key) throws SQLException {
        String sql = "SELECT * FROM session WHERE `key` = ?";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getSession(rs);
            }
            return null;
        }
    }

    public void deleteSession(Session session) throws SQLException {
        String sql = "DELETE FROM session WHERE `key` = ?";
        try (Connection con = DatabaseService.getDataSource().getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, session.getKey());
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
