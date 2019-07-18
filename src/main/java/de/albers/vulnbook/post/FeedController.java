package de.albers.vulnbook.post;

import de.albers.vulnbook.post.exceptions.PostEmptyException;
import de.albers.vulnbook.post.services.RetrievePostService;
import de.albers.vulnbook.post.services.SubmitPostService;
import de.albers.vulnbook.user.services.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String feedPage(@RequestParam(value = "numPosts", required=false, defaultValue = "20") int numPosts, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                model.addAttribute("loggedIn", true);
            } else {
                response.sendRedirect("/login");
            }
            long lastestPostId = retrievePostService.getLatestPostId();
            List<Post> posts = retrievePostService.getNumPosts(lastestPostId, numPosts);
            model.addAttribute("posts", posts);
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
        }
        return "sites/feed.html";
    }
}
