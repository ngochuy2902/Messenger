package com.msg.messenger.service;

import com.msg.messenger.domain.User;
import com.msg.messenger.exception.ObjectNotFoundException;
import com.msg.messenger.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getByUsername(final String username) {
        log.info("Get account by username #{}", username);

        return userRepository.findByUsername(username)
                             .orElse(null);
    }

    @Transactional
    public void save(final User user) {
        log.info("Save user #{}", user);

        userRepository.save(user);
    }
}
