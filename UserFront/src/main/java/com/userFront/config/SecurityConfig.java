package com.userFront.config;

import java.security.SecureRandom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.userFront.domain.security.CustomAuthenticationFailureHandler;
import com.userFront.domain.security.LoginSuccessHandler;
import com.userFront.service.UserServiceImpl.UserSecurityService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityService userSecurityService;
    
    @Autowired 
    private LoginSuccessHandler loginSuccessHandler;

    private static final String SALT = "salt"; // Salt should be protected carefully

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }
    
    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(-1);
        return commonsMultipartResolver;
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**/*",
            "/console/**",
            "/signup",
            "/index-error",
            "/h2-ui/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().
                antMatchers(PUBLIC_MATCHERS).
                permitAll().

                anyRequest().authenticated();

        http
                .csrf().disable().cors().disable()  
                .formLogin().failureHandler(authenticationFailureHandler()).successHandler(loginSuccessHandler).loginPage("/index").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe();
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }


}
