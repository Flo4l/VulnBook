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
import java.util.List;

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
        posts.stream().forEach(p -> {
            try {
                User user = loginUserService.getUserById(p.getUserId());
                JSONObject jsonPost = new JSONObject(p.toMap());
                JSONObject jsonUser = new JSONObject(user.toMap());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonPost);
                jsonArray.put(jsonUser);
                jsonPosts.put(jsonArray);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return jsonPosts.toString();
    }
}
