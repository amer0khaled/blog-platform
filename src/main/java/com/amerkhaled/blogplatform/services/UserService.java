package com.amerkhaled.blogplatform.services;

import com.amerkhaled.blogplatform.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
