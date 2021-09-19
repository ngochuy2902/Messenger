package com.msg.messenger.service;

import com.msg.messenger.domain.UserRole;
import com.msg.messenger.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(final UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public void save(final UserRole userRole) {
        log.info("Save userRole #{}", userRole);

        userRoleRepository.save(userRole);
    }
}
