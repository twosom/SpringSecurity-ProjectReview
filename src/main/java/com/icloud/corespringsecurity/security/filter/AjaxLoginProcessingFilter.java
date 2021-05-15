package com.icloud.corespringsecurity.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.security.token.AjaxAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private final String X_REQUESTED_WITH = "X-Requested-With";


    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
                                                throws AuthenticationException, IOException, ServletException {

        if (!HttpMethod.POST.name().equals(request.getMethod()) || !isAjax(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        AccountDto accountDto = new ObjectMapper().readValue(request.getInputStream(), AccountDto.class);

        if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password must not by empty");
        }

        return getAuthenticationManager()
                .authenticate(
                        new AjaxAuthenticationToken(
                                accountDto.getUsername(),
                                accountDto.getPassword()
                        )
                );
    }

    private boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }
}
