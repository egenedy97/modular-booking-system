package com.example.modular_booking_system.user.repository;

import com.example.modular_booking_system.user.model.UserRole;
import com.example.modular_booking_system.user.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}