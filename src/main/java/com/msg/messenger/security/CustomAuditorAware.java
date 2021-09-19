package com.msg.messenger.security;

import com.msg.messenger.constant.Constant;
import com.msg.messenger.helper.SecurityHelper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware
    implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityHelper.getUsername().orElse(Constant.SYSTEM));
    }
}
