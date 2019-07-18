package de.albers.vulnbook.post.services;

import de.albers.vulnbook.post.Post;
import de.albers.vulnbook.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RetrievePostService {

    private PostRepository postRepository;

    @Autowired
    public RetrievePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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
}
