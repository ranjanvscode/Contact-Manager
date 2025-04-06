package com.scm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.forms.UserUpdateForm;
import com.scm.helper.AppConstant;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

   @Autowired
   UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {   
        List<User> allUser = userService.getAllUsers();
        model.addAttribute("allUserList",allUser);
        return "user/dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userView/{userId}")
    public String userView(@PathVariable String userId, Model model){
      User user = userService.getUserById(userId).get();
      model.addAttribute("userData", user);
      return "user/userView";
    }

    @PostMapping("/update/{userId}")
    public String updateUser(@ModelAttribute UserUpdateForm userUpdateForm,@PathVariable String userId,Model model,HttpSession session){
      User user = userService.getUserById(userId).get();

      user.setEnabled(userUpdateForm.isEnabled());
      user.setEmailVarified(userUpdateForm.isEmailVarified());
      user.setPhoneVarified(userUpdateForm.isPhoneVarified());

      List<String> newRoleList = new ArrayList<>(user.getRoleList());

      newRoleList.clear();

      if (!userUpdateForm.getRoleList().isEmpty()) {

        newRoleList.add(AppConstant.ROLE_USER);
        newRoleList.add(AppConstant.ROLE_ADMIN);
      }else{

        newRoleList.add(AppConstant.ROLE_USER);
      }

      user.setRoleList(newRoleList);
      user = userService.updateUser(user);
      model.addAttribute("userData", user);
      Message msg = Message.builder().content("User updated successfully!").type(MessageType.green).build();
      session.setAttribute("message", msg);

      return "user/userView";
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
