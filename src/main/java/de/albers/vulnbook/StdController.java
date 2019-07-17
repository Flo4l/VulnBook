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

    @GetMapping({"/home", "/"})
    public String homePage(Model model) {
        return "sites/home.html";
    }

}
