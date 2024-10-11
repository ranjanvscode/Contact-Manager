package com.scm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard()
    { 
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(){

      return "user/profile";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){

      return "error/access-denied";
    }

}
