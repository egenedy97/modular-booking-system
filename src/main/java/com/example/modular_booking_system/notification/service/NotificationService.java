package com.example.modular_booking_system.notification.service;

import com.example.modular_booking_system.notification.model.Notification;
import com.example.modular_booking_system.notification.model.NotificationStatus;
import com.example.modular_booking_system.notification.model.NotificationType;
import java.util.List;

public interface NotificationService {
        Notification createNotification(
                        String message,
                        NotificationType notificationType,
                        long userId);

        Notification updateNotification(
                        Long notificationId,
                        NotificationStatus notificationStatus);

        List<Notification> createMultiNotification();
}
