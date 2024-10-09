package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstant;
import com.scm.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Component
public class OAuthAuthenticationSuccesshandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
        
        var authenticationTocken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = authenticationTocken.getAuthorizedClientRegistrationId();

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // user.getAttributes().forEach((K,V)->System.out.println(K+"  "+V)); // Print all attributes of provider

        String name = user.getAttribute("name").toString();
        String email=null;

        User user1 = new User();
        user1.setId(UUID.randomUUID().toString());
        user1.setName(name);
        user1.setPassword("Password");
        user1.setEmailVarified(true);
        user1.setEnabled(true);
        user1.setPhoneNo("");
        user1.setPhoneVarified(false);
        user1.setRoleList(List.of(AppConstant.ROLE_USER));

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

            email = user.getAttribute("email").toString();
            String picture = user.getAttribute("picture").toString();

            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setProviderId(user.getName());
            user1.setProvider(Providers.GOOGLE);

        }else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            
            email = user.getAttribute("login").toString()+"@gmail.com";
            String picture = user.getAttribute("avatar_url").toString();

            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setProviderId(user.getName());
            user1.setProvider(Providers.GITHUB);

        }else{
                System.out.println("Unknown Provider");
        }

        User userExist = userRepo.findByEmail(email).orElse(null);

        if (userExist==null) userRepo.save(user1);
         

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
