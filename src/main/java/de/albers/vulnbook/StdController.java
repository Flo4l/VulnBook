package de.albers.vulnbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StdController {

    @GetMapping("/")
    public String homePage(Model model) {
        return "sites/index.html";
    }
}
