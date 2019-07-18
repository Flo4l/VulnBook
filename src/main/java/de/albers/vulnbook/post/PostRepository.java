package de.albers.vulnbook.post;

import de.albers.vulnbook.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {

    public void savePost(Post post) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO post (time, text, likes, userid) VALUES ('" + post.getTime() + "', '" + post.getText() + "', " + post.getLikes() + ", " + post.getUserid() + ")";
        stmt.execute(sql);
    }

    public void updateLikes(Post post) throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "UPDATE post SET likes = " + post.getLikes() + " WHERE postid =" + post.getPostId();
        stmt.execute(sql);
    }

    public List<Post> getAllPosts() throws SQLException {
        Connection con = DatabaseService.getDataSource().getConnection();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM post";
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Post> posts = new ArrayList<>();
        while(rs.next()) {
            posts.add(getPost(rs));
        }
        return posts;
    }

    private Post getPost(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("postid");
        LocalDateTime time = resultSet.getTimestamp("time").toLocalDateTime();
        String text = resultSet.getString("text");
        long likes = resultSet.getLong("likes");
        long userid = resultSet.getLong("userid");
        return new Post(id, time, text, likes, userid);
    }
}
