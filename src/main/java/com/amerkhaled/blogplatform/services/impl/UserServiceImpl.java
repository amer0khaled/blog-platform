package com.amerkhaled.blogplatform.services.impl;

import com.amerkhaled.blogplatform.domain.entities.User;
import com.amerkhaled.blogplatform.repositories.UserRepository;
import com.amerkhaled.blogplatform.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " does not exist"));
    }
}
