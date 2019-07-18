package de.albers.vulnbook.post;

import de.albers.vulnbook.post.exceptions.PostEmptyException;
import de.albers.vulnbook.post.services.SubmitPostService;
import de.albers.vulnbook.user.services.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FeedController {

    private LoginUserService loginUserService;
    private SubmitPostService submitPostService;

    @Autowired
    public FeedController(LoginUserService loginUserService, SubmitPostService submitPostService) {
        this.loginUserService = loginUserService;
        this.submitPostService = submitPostService;
    }

    @GetMapping("/feed")
    public String feedPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                model.addAttribute("loggedIn", true);
            } else {
                response.sendRedirect("/login");
            }
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
