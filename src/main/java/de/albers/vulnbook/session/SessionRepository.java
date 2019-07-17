package de.albers.vulnbook.session;

import de.albers.vulnbook.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class SessionRepository {

    public void createSesion(Session session) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO session (`key`, expires, userid) VALUES ('" + session.getKey() + "', '" + session.getExpires() + "', " + session.getUserId() + ")";
        stmt.execute(sql);
    }

    public Session getSessionByKey(String key) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM session WHERE `key` = '" + key + "'";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return getSession(rs);
        }
        return null;
    }

    private Session getSession(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("sessionid");
        String key = resultSet.getString("key");
        LocalDateTime expires = resultSet.getTimestamp("expires").toLocalDateTime();
        long userid = resultSet.getLong("userid");
        return new Session(id, key, expires, userid);
    }
}
