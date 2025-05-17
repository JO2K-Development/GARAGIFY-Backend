package com.jo2k.garagify.user.service;


import com.jo2k.garagify.common.exception.ObjectNotFoundException;
import com.jo2k.garagify.config.security.AuthContext;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    @Transactional
    public UUID findOrCreateByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email))).getId();
    }

    public User getCurrentUser() {
        UUID uuid = AuthContext.getUserId();
        return getUserById(uuid);
    }

    private User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ObjectNotFoundException("User with uuid " + uuid + " not found")
        );
    }
}
