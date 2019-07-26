package de.albers.vulnbook.post;

import de.albers.vulnbook.post.exceptions.AlreadyLikedException;
import de.albers.vulnbook.post.exceptions.PostEmptyException;
import de.albers.vulnbook.post.services.RetrievePostService;
import de.albers.vulnbook.post.services.SubmitPostService;
import de.albers.vulnbook.user.services.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@Controller
public class FeedController {

    private LoginUserService loginUserService;
    private SubmitPostService submitPostService;
    private RetrievePostService retrievePostService;

    @Autowired
    public FeedController(LoginUserService loginUserService, SubmitPostService submitPostService, RetrievePostService retrievePostService) {
        this.loginUserService = loginUserService;
        this.submitPostService = submitPostService;
        this.retrievePostService = retrievePostService;
    }

    @GetMapping(value = "/feed")
    public String feedPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                model.addAttribute("loggedIn", true);
            } else {
                response.sendRedirect("/login");
            }
            addLatestPosts(model);
            return "sites/feed.html";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/feed")
    public String submitPost(String text, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                model.addAttribute("loggedIn", true);
            } else {
                response.sendRedirect("/login");
                return null;
            }
            submitPostService.checkPostEmpty(text);
            Post post = submitPostService.createPost(text, request);
            submitPostService.submitPost(post);
        } catch (PostEmptyException e) {
            model.addAttribute("postEmpty", true);
        } catch (Exception e) {
            model.addAttribute("internalError", true);
            e.printStackTrace();
        }
        try{
            addLatestPosts(model);
        } catch (Exception e) {
            model.addAttribute("internalError", true);
            e.printStackTrace();
        }
        return "/sites/feed.html";
    }

    @ResponseBody
    @PostMapping("/posts/retrieve")
    public String getNumPosts(@RequestParam(name = "idFirst") long idFirst, @RequestParam(name = "numPosts") int numPosts, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                return retrievePostService.getNumPostsAsJson(idFirst, numPosts);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    @PostMapping("/post/like/{id}")
    public void likePost(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                submitPostService.likePost(id, request);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (AlreadyLikedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLatestPosts(Model model) throws SQLException {
        long lastestPostId = retrievePostService.getLatestPostId();
        List<Post> posts = retrievePostService.getNumPosts(lastestPostId, 20);
        long oldestPostId = 0;
        if(!posts.isEmpty()) {
            oldestPostId = posts.get(posts.size()-1).getPostId();
        }
        model.addAttribute("latestPostId", oldestPostId);
        model.addAttribute("posts", posts);
    }

}
