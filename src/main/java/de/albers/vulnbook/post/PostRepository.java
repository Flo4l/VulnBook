package de.albers.vulnbook.post;

import de.albers.vulnbook.DatabaseService;
import de.albers.vulnbook.user.User;
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
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "INSERT INTO post (time, text, likes, userid) VALUES ('" + post.getTime() + "', '" + post.getText() + "', " + post.getLikes() + ", " + post.getUserId() + ")";
            stmt.execute(sql);
        }
    }

    public void saveLike(User user, Post post) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "INSERT INTO user_likes_post (userid, postid) VALUES(" + user.getUserId() + ", " + post.getPostId() + ")";
            stmt.execute(sql);
        }
    }

    public void updateLikes(Post post) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "UPDATE post SET likes = (SELECT COUNT(postid) FROM user_likes_post WHERE postid = " + post.getPostId() + ") WHERE postid = " + post.getPostId();
            stmt.execute(sql);
        }
    }

    public boolean checkUserLikedPost(User user, Post post) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM user_likes_post WHERE userid = " + user.getUserId() + " AND postid = " + post.getPostId();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        }
    }

    public long getLatestPostId() throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT MAX(postid) as id FROM post";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong("id");
        }
    }

    public List<Post> getNumPosts(long idFirst, int numPosts) throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM post WHERE postid <= " + idFirst + " ORDER BY postid DESC LIMIT " + numPosts;
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(getPost(rs));
            }
            return posts;
        }
    }

    public Post getPostById(long postId) throws SQLException{
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM post WHERE postid = " + postId;
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return getPost(rs);
            }
            return null;
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        try (Connection con = DatabaseService.getDataSource().getConnection(); Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM post ORDER BY postid DESC";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(getPost(rs));
            }
            return posts;
        }
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
