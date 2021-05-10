package com.icloud.corespringsecurity.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.formLogin();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = encoder.encode("1111");

        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
        auth.inMemoryAuthentication().withUser("manager").password(password).roles("USER", "MANAGER");
        auth.inMemoryAuthentication().withUser("admin").password(password).roles("USER", "MANAGER", "ADMIN");
    }
}
