package com.icloud.corespringsecurity.security.configs;

import com.icloud.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import com.icloud.corespringsecurity.security.factory.UrlResourcesMapFactoryBean;
import com.icloud.corespringsecurity.security.handler.form.FormAccessDeniedHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationFailureHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationSuccessHandler;
import com.icloud.corespringsecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.icloud.corespringsecurity.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.Filter;
import java.util.Arrays;

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

    private final UrlResourcesMapFactoryBean urlResourcesMapFactoryBean;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        accessDeniedHandler.setErrorPage("/denied");

        http.authorizeRequests()
                .antMatchers("/", "/users", "user/login/**", "/login*").permitAll()
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

        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor fsi = new FilterSecurityInterceptor();

        fsi.setAuthenticationManager(authenticationManager());
        fsi.setSecurityMetadataSource(urlFilterSecurityMetadataSource());
        fsi.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(new RoleVoter())));

        return fsi;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean.getObject());
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
