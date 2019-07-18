package de.albers.vulnbook;

import de.albers.vulnbook.user.services.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class StdController {

    private LoginUserService loginUserService;

    @Autowired
    public StdController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
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

    @GetMapping({"/home", "/"})
    public String homePage(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                response.sendRedirect("/feed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sites/home.html";
    }

}
