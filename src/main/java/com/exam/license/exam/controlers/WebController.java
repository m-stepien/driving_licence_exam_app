package com.exam.license.exam.controlers;

import com.exam.license.exam.models.User;
import com.exam.license.exam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        return "index";
    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home2(){
        return home();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginV(){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerView(){
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute User user){
        this.userService.register(user);
        return "register";
    }


    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String exam(Model model, Principal principal){
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "exam";
    }
}
