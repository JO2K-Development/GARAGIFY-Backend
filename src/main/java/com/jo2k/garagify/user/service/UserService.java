package com.jo2k.garagify.user.service;


import com.jo2k.garagify.common.exception.ObjectNotFoundException;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    @Transactional
    public UUID findOrCreateByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    log.info("User found with email: {} (id: {})", email, user.getId());
                    return user;
                })
                .orElseGet(() -> {
                    User newUser = new User(email);
                    userRepository.save(newUser);
                    log.info("Created new user with email: {} (id: {})", email, newUser.getId());
                    return newUser;
                })
                .getId();
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) auth.getPrincipal();
        return getUserById(UUID.fromString(userId));
    }

    private User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ObjectNotFoundException("User with uuid " + uuid + " not found")
        );
    }
}
