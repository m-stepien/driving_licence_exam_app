package com.exam.license.exam.controlers;

import com.exam.license.exam.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private LoginService loginService;

    @Autowired
    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }
}
