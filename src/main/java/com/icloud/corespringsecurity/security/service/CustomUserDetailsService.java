package com.icloud.corespringsecurity.security.service;

import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        List<SimpleGrantedAuthority> authorities = account.getRoleList()
                .stream().map(Role::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AccountContext(account, authorities);
    }
}
