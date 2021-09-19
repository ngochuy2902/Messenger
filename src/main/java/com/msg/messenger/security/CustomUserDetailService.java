package com.msg.messenger.security;

import com.msg.messenger.domain.User;
import com.msg.messenger.domain.UserRole;
import com.msg.messenger.repository.UserRepository;
import com.msg.messenger.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomUserDetailService
    implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailService(final UserRepository userRepository,
                                   final UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username)
        throws UsernameNotFoundException {
        log.info("Authenticating with username #{}", username);

        return userRepository.findByUsername(username)
                             .map(this::createUserDetail)
                             .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private UserDetails createUserDetail(final User user) {
        Set<UserRole> userRoles = userRoleRepository.fetchByUserId(user.getId());

        return CustomUserDetail.builder()
                               .id(user.getId())
                               .username(user.getUsername())
                               .password(user.getPassword())
                               .authorities(userRoles.stream()
                                                     .map(ur -> new SimpleGrantedAuthority(ur.getName().name()))
                                                     .collect(Collectors.toList()))
                               .build();
    }
}
