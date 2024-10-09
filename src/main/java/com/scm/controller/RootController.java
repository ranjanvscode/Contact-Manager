package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.ServiceImpl.UserServiceImpl;
import com.scm.entities.User;
import com.scm.helper.GetEmail;

@ControllerAdvice
public class RootController {

    @Autowired
    UserServiceImpl userRepo;

    @ModelAttribute
    public void loggedInUserInfo(Model model,Authentication authentication){

        if (authentication==null) {
            return;
        }else{

            String email = GetEmail.getEmailOfLoggedInUser(authentication);
            User user = userRepo.getUserByEmail(email);
            model.addAttribute("user", user);
        }
    }
    
}
