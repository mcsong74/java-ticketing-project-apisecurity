package com.cybertek.config;

import com.cybertek.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //in order to use @PreAuthorize("hasAuthority('Admin')")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // in order to enable security, need to create this class,
    // 1. extends WebSecurityConfigurerAdapter
    // 2. annotation - @Configuration, @EnableWebSecurity
    // 3. @Override methods
    @Autowired
    private SecurityFilter securityFilter;

    //no login form, need below
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String[] permittedUrls = {
            "/authenticate",
            "/confirmation",
            "/api/v1/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http    //ex. if there is angular, has diff port using it, and csrf need to be disabled
                .csrf()
                .disable() //to config custom config manually, need to be disabled//
                .authorizeRequests()
//                .antMatchers("/authenticate", "/create-user") //
                .antMatchers(permittedUrls) // method for public access
                .permitAll() //all user can access
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //do not keep cookies


        //run security filter before send any api request
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
