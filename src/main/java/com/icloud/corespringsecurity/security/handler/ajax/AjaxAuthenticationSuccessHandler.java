package com.icloud.corespringsecurity.security.handler.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.security.token.AjaxAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Account account = (Account) authentication.getPrincipal();
        ((AjaxAuthenticationToken) authentication).setDetails(new WebAuthenticationDetails(request));

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        /* ObjectMapper 가 JSON 형식으로 변환해서 Response 를 이용하여 Client 에 전달 */
        new ObjectMapper().writeValue(response.getWriter(), "loginOK");
    }
}
