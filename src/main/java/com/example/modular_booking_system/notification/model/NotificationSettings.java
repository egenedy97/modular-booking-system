package com.example.modular_booking_system.notification.model;

import java.io.Serializable;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationSettingsId")
    private Long id;

    @Column(name = "emailNotificationsEnabled", nullable = false)
    private boolean emailNotificationsEnabled;

    @Column(name = "smsNotificationsEnabled", nullable = false)
    private boolean smsNotificationsEnabled;

}
