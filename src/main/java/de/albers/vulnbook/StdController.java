package de.albers.vulnbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StdController {

    @GetMapping("/feed")
    public String feedPage(Model model) {
        return "sites/feed.html";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "sites/login.html";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "sites/register.html";
    }

    @GetMapping({"/home", "/"})
    public String homePage(Model model) {
        return "sites/home.html";
    }

}
