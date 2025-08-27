package com.example.modular_booking_system.user.repository;

import com.example.modular_booking_system.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);

    @Query("select c.email from User u join u.contact c where u.userId = :userId")
    Optional<String> findEmailByUserId(@Param("userId") Long userId);

}