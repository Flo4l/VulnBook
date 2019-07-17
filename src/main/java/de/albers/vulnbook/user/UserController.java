package de.albers.vulnbook.user;

import de.albers.vulnbook.user.exceptions.AlreadyRegisteredException;
import de.albers.vulnbook.user.exceptions.FieldEmptyException;
import de.albers.vulnbook.user.exceptions.UnequalPasswordsException;
import de.albers.vulnbook.user.services.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;

@Controller
public class UserController {

    private RegisterUserService registerUserService;

    @Autowired
    public UserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "sites/login.html";
    }

    @PostMapping("/login")
    public String loginUser(User user, BindingResult bindingResult, Model model) {
        //TODO
        return "sites/login.html";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        return "sites/register.html";
    }

    @PostMapping("/register")
    public String registerUser(User user, String pass, BindingResult bindingResult, Model model) {
        try {
            registerUserService.checkFieldEmpty(user, pass);
            registerUserService.checkPasswordEqual(user.getPassword(), pass);
            registerUserService.checkAlreadyRegistered(user);
            registerUserService.registerUser(user);
            model.addAttribute("success", true);
        } catch (FieldEmptyException e) {
            model.addAttribute("fieldEmpty", true);
        } catch (UnequalPasswordsException e) {
            model.addAttribute("unequalPasswords", true);
        }catch (AlreadyRegisteredException e) {
            model.addAttribute("alredyRegistered", true);
        } catch (SQLException e) {
            model.addAttribute("sqlError", true);
            e.printStackTrace();
        }
        return "sites/register.html";
    }
}
