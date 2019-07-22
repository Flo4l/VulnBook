package de.albers.vulnbook.post.services;

import de.albers.vulnbook.post.Post;
import de.albers.vulnbook.post.PostRepository;
import de.albers.vulnbook.user.User;
import de.albers.vulnbook.user.services.LoginUserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RetrievePostService {

    private PostRepository postRepository;
    private LoginUserService loginUserService;

    @Autowired
    public RetrievePostService(PostRepository postRepository, LoginUserService loginUserService) {
        this.postRepository = postRepository;
        this.loginUserService = loginUserService;
    }

    public List<Post> getAllPosts() throws SQLException {
        return postRepository.getAllPosts();
    }

    public List<Post> getNumPosts(long idFirst, int numPosts) throws SQLException {
        return postRepository.getNumPosts(idFirst, numPosts);
    }

    public long getLatestPostId() throws SQLException {
        return postRepository.getLatestPostId();
    }

    public String getNumPostsAsJson(long idFirst, int numPosts) throws SQLException {
        List<Post> posts = getNumPosts(idFirst, numPosts);
        JSONArray jsonPosts = new JSONArray();
        posts.forEach(p -> {
            try {
                User user = loginUserService.getUserById(p.getUserId());
                Map<String, String> map = new HashMap<>();
                map.put("postid", String.valueOf(p.getPostId()));
                map.put("time", p.getTimeText());
                map.put("text", p.getText());
                map.put("likes", String.valueOf(p.getLikes()));
                map.put("username", user.getUsername());
                JSONObject jsonPost = new JSONObject(map);
                jsonPosts.put(jsonPost);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return jsonPosts.toString();
    }
}
