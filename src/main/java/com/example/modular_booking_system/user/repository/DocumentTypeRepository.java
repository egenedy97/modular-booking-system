package com.example.modular_booking_system.user.repository;

import com.example.modular_booking_system.user.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}