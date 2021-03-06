package com.icloud.corespringsecurity.security.configs;

import com.icloud.corespringsecurity.security.factory.UrlResourcesMapFactoryBean;
import com.icloud.corespringsecurity.security.filter.PermitAllFilter;
import com.icloud.corespringsecurity.security.handler.form.FormAccessDeniedHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationEntryPoint;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationFailureHandler;
import com.icloud.corespringsecurity.security.handler.form.FormAuthenticationSuccessHandler;
import com.icloud.corespringsecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.icloud.corespringsecurity.security.provider.FormAuthenticationProvider;
import com.icloud.corespringsecurity.security.voter.IpAddressVoter;
import com.icloud.corespringsecurity.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ResourcesService resourcesService;
    private final FormAuthenticationProvider authenticationProvider;
    private final FormAuthenticationEntryPoint entryPoint;

    /**
     * Handler
     */
    private final FormAuthenticationSuccessHandler successHandler;
    private final FormAuthenticationFailureHandler failureHandler;
    private final FormAccessDeniedHandler accessDeniedHandler;

    private final UrlResourcesMapFactoryBean urlResourcesMapFactoryBean;
    private final IpAddressVoter ipAddressVoter;

    private String[] permitAllResources = {"/", "/login", "/user/login/**", "/api/login", "/js/**", "/css/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        accessDeniedHandler.setErrorPage("/denied");

        http.authorizeRequests()
                .anyRequest().authenticated();


        http.addFilterBefore(customSecurityFilterInterceptor(), FilterSecurityInterceptor.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint);
    }

    @Bean
    public Filter customSecurityFilterInterceptor() throws Exception {
        PermitAllFilter fsi = new PermitAllFilter(permitAllResources);
        fsi.setAuthenticationManager(authenticationManager());
        fsi.setAccessDecisionManager(getAccessDecisionManager());
        fsi.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());

        return fsi;
    }

    private AffirmativeBased getAccessDecisionManager() {
        return new AffirmativeBased(getDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getDecisionVoters() {

        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(ipAddressVoter);
        accessDecisionVoters.add(roleVoter());

        return accessDecisionVoters;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean.getObject(), resourcesService);
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
