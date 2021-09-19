package com.msg.messenger.helper;

import com.msg.messenger.enums.RoleType;
import com.msg.messenger.security.CustomUserDetail;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SecurityHelper {

    private SecurityHelper() {
    }

    public static Long getUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            return customUserDetail.getId();
        }

        return null;
    }

    public static Optional<String> getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            return Optional.ofNullable(customUserDetail.getUsername());
        }

        return Optional.empty();
    }

    public static boolean hasRole(final RoleType roleType) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (ObjectUtils.isNotEmpty(authentication)) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority.getAuthority().equalsIgnoreCase(roleType.name())) {
                    return true;
                }
            }
        }

        return false;
    }
}
