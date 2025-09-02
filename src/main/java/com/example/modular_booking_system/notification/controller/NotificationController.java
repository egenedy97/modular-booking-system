package com.example.modular_booking_system.notification.controller;

import com.example.modular_booking_system.notification.model.Notification;
import com.example.modular_booking_system.notification.model.NotificationType;
import com.example.modular_booking_system.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/notifications")
    public ResponseEntity<?> createNotification() {
        List<Notification> notifications = notificationService.createMultiNotification();
        return ResponseEntity.ok().body(notifications);
    }
}
