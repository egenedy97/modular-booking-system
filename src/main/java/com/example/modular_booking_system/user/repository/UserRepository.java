package com.example.modular_booking_system.user.repository;

import com.example.modular_booking_system.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}