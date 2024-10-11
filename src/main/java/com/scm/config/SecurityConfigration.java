package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.service.CustomUserDetailService;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) 
public class SecurityConfigration {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    OAuthAuthenticationSuccesshandler handler;

    // @Bean
    // public InMemoryUserDetailsManager inMemoryUserDetailsManager()
    // {
    //     UserDetails user1 = User.withDefaultPasswordEncoder()
    //                             .username("user@gmail.com")
    //                             .password("password")
    //                             .roles("USER")
    //                             .build();
    
    //     return new InMemoryUserDetailsManager(user1);
    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       
        httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize->{ authorize
                                            .requestMatchers("/user/**").authenticated()
                                            .requestMatchers("/dashboard").hasRole("ADMIN")
                                            .anyRequest().permitAll();
                                        });

        httpSecurity.formLogin(form->
        
            form.loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/user/profile", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            ).logout((logout) -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout=true"));

        httpSecurity.oauth2Login(auth->
                                    {
                                        auth.loginPage("/login");
                                        auth.successHandler(handler);
                                    });

        httpSecurity.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()));

        return  httpSecurity.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, accessDeniedException) -> response.sendRedirect("/user/access-denied");
    }

    @Bean
    AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
