package com.example.modular_booking_system.user.repository;

import com.example.modular_booking_system.user.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmail(String email);
    boolean existsByEmail(String email);
}