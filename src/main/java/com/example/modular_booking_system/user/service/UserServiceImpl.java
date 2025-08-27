package com.example.modular_booking_system.user.service;

import com.example.modular_booking_system.user.model.User;
import com.example.modular_booking_system.user.repository.ContactRepository;
import com.example.modular_booking_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return contactRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

}
