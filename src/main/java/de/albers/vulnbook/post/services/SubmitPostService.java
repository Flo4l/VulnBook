package de.albers.vulnbook.post.services;

import de.albers.vulnbook.post.Post;
import de.albers.vulnbook.post.PostRepository;
import de.albers.vulnbook.post.exceptions.AlreadyLikedException;
import de.albers.vulnbook.post.exceptions.PostEmptyException;
import de.albers.vulnbook.session.SessionService;
import de.albers.vulnbook.user.User;
import de.albers.vulnbook.user.services.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class SubmitPostService {

    private PostRepository postRepository;
    private SessionService sessionService;
    private LoginUserService loginUserService;

    @Autowired
    public SubmitPostService(PostRepository postRepository, SessionService sessionService, LoginUserService loginUserService) {
        this.postRepository = postRepository;
        this.sessionService = sessionService;
        this.loginUserService = loginUserService;
    }

    public void checkPostEmpty(String text) throws PostEmptyException {
        if(text.equals("")) {
            throw new PostEmptyException();
        }
    }

    public Post createPost(String text, HttpServletRequest request) throws SQLException {
        long userid = loginUserService.getLoggedInUser(request).getUserId();
        long likes = 0;
        LocalDateTime time = LocalDateTime.now();
        return new Post(time, text, likes, userid);
    }

    public void submitPost(Post post) throws SQLException {
        postRepository.savePost(post);
    }

    public void likePost(long postId, HttpServletRequest request) throws SQLException {
        User user = loginUserService.getLoggedInUser(request);
        Post post = postRepository.getPostById(postId);
        if(postRepository.checkUserLikedPost(user, post)) {
            throw new AlreadyLikedException(user.getUsername() + " already likes post nr. " + postId);
        }
        post.setLikes(post.getLikes() + 1);
        postRepository.saveLike(user, post);
        postRepository.updateLikes(post);
    }
}
