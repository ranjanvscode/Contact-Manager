package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class GetEmail {

    public static String getEmailOfLoggedInUser(Authentication authentication){

        String username="";

        if (authentication instanceof OAuth2AuthenticationToken) {

            var authenticationTocken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = authenticationTocken.getAuthorizedClientRegistrationId();
            DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
            
            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            
                username = user.getAttribute("email").toString();

            }else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
    
                username = user.getAttribute("login").toString()+"@gmail.com";
            }

            return username;
        }else{

            return authentication.getName();
        }
    }
}
