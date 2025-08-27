package com.example.modular_booking_system.user.service;

import com.example.modular_booking_system.user.model.User;

public interface UserService {
    User findById(Long id);
    User save(User user);
    boolean existsByEmail(String email);
}
