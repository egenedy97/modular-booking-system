package com.example.modular_booking_system.user.service;

import com.example.modular_booking_system.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

}
