package com.icloud.corespringsecurity.security.voter;

import com.icloud.corespringsecurity.service.AccessIpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessIpVoter implements AccessDecisionVoter<Object> {

    private final AccessIpService accessIpService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        List<String> ipList = accessIpService.getIpList();

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String ipAddress = details.getRemoteAddress();

        if (ipList.contains(ipAddress)) {
            return ACCESS_ABSTAIN;
        }

        throw new AccessDeniedException("Invalid IP Address");
    }
}
