package de.albers.vulnbook.user;

import de.albers.vulnbook.user.exceptions.AlreadyRegisteredException;
import de.albers.vulnbook.user.exceptions.FieldEmptyException;
import de.albers.vulnbook.user.exceptions.IncorrectLoginException;
import de.albers.vulnbook.user.exceptions.UnequalPasswordsException;
import de.albers.vulnbook.user.services.LoginUserService;
import de.albers.vulnbook.user.services.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private RegisterUserService registerUserService;
    private LoginUserService loginUserService;

    @Autowired
    public UserController(RegisterUserService registerUserService, LoginUserService loginUserService) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                response.sendRedirect("/feed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sites/login.html";
    }

    @GetMapping("/logout")
    public void logUserOut(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            loginUserService.logoutUser(request, response);
            response.sendRedirect("/home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(loginUserService.checkLoggedIn(request)) {
                response.sendRedirect("/feed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sites/register.html";
    }

    @PostMapping("/login")
    public String loginUser(User user, Model model, HttpServletResponse response) {
        try {
            loginUserService.checkFieldEmpty(user);
            loginUserService.checkCorrectLogin(user);
            loginUserService.loginUser(user, response);
        } catch (FieldEmptyException e) {
            model.addAttribute("fieldEmpty", true);
        } catch (IncorrectLoginException e) {
            model.addAttribute("incorrectLogin", true);
        } catch (Exception e) {
            model.addAttribute("internalError", true);
            e.printStackTrace();
        }
        return "sites/login.html";
    }

    @PostMapping("/register")
    public String registerUser(User user, String pass, Model model) {
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
        } catch (Exception e) {
            model.addAttribute("internalError", true);
            e.printStackTrace();
        }
        return "sites/register.html";
    }
}
