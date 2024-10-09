package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.ServiceImpl.UserServiceImpl;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class MyController {

    @Autowired
    UserServiceImpl userService = new UserServiceImpl();

    @GetMapping("/")
    public String general() {
        return "redirect:/home";
    }

    @GetMapping("/base")
    public String getBase() {
        return "base";
    }

    @GetMapping("/contact")
    public String contact(){

        return "contact";
    }

    @GetMapping("/service")
    public String service(){

        return "service";
    }

    @GetMapping("/home")
    public String getHome() {

        return "home";
    }

    @GetMapping("/about")
    public String getAbout() {

        return "about";
    }

    @GetMapping("/signup")
    public String getSignup(Model m) {

        UserForm userFrom = new UserForm();
        m.addAttribute("userForm", userFrom);

        return "signup";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute UserForm userForm, BindingResult errorResult ,HttpSession session) {


        if (errorResult.hasErrors()) {
            return "signup";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNo(userForm.getPhoneNo());
        user.setProfilePic(" ");

        User data = userService.saveUser(user);
        Message msg=null;
        if (data==null) {
            
            msg = Message.builder().content("User already exist").type(MessageType.green).build();
        }else{

            msg = Message.builder().content("Registration Successful").type(MessageType.green).build();
        }

        session.setAttribute("message", msg);

        return "redirect:/signup";
    }
    
}
