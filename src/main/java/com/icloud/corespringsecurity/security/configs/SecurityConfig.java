package com.icloud.corespringsecurity.security.configs;

import com.icloud.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import com.icloud.corespringsecurity.security.handler.form.FormAccessDeniedHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationFailureHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationSuccessHandler;
import com.icloud.corespringsecurity.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final FormAuthenticationProvider authenticationProvider;

    private final FormAuthenticationDetailsSource authenticationDetailsSource;

    /**
     * Handler
     */
    private final FormAuthenticationSuccessHandler successHandler;
    private final FormAuthenticationFailureHandler failureHandler;
    private final FormAccessDeniedHandler accessDeniedHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        accessDeniedHandler.setErrorPage("/denied");

        http.authorizeRequests()
                .antMatchers("/", "/users", "user/login.**", "/login*").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource)
                .defaultSuccessUrl("/")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll();

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }




}
