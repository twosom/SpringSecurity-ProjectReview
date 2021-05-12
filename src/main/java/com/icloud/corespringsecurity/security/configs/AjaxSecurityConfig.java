package com.icloud.corespringsecurity.security.configs;

import com.icloud.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import com.icloud.corespringsecurity.security.handler.ajax.AjaxAccessDeniedHandler;
import com.icloud.corespringsecurity.security.handler.ajax.AjaxAuthenticationFailureHandler;
import com.icloud.corespringsecurity.security.handler.ajax.AjaxAuthenticationSuccessHandler;
import com.icloud.corespringsecurity.security.handler.ajax.AjaxLoginAuthenticationEntryPoint;
import com.icloud.corespringsecurity.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Slf4j
@Order(0)
@RequiredArgsConstructor
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;

    private final AjaxAuthenticationSuccessHandler successHandler;
    private final AjaxAuthenticationFailureHandler failureHandler;

    private final AjaxLoginAuthenticationEntryPoint authenticationEntryPoint;
    private final AjaxAccessDeniedHandler accessDeniedHandler;


    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());

        /*
            http.formLogin() 메소드와는 다르게
            사용자가 정의한 필터에는 Bean 객체에다가
            successHandler, failureHandler 를 설정한다.
         */
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .anyRequest().authenticated();
        http.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        /*
            ExceptionTranslationFilter 에서 발생하는 예외를 처리하기 위한 handler 설정
         */
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider);
    }
}
