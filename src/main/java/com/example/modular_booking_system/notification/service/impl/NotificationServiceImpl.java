package com.example.modular_booking_system.notification.service.impl;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.notification.model.Notification;
import com.example.modular_booking_system.notification.model.NotificationStatus;
import com.example.modular_booking_system.notification.model.NotificationType;
import com.example.modular_booking_system.notification.repository.NotificationRepository;
import com.example.modular_booking_system.notification.service.NotificationService;
import com.example.modular_booking_system.user.model.User;
import com.example.modular_booking_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final NotificationRepository notificationRepository;

    private final RabbitTemplate rabbitTemplate;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    @Override
    public Notification createNotification(
            String message,
            NotificationType notificationType,
            long userId) {
        // Implementation here
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Notification notification = Notification.builder()
                .message(message)
                .type(notificationType)
                .status(NotificationStatus.PENDING)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .user(user)
                .build();

        notificationRepository.save(notification);

        if (notificationType == NotificationType.EMAIL) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.EMAIL_NOTIFICATION_ROUTING_KEY, notification);
        } else if (notificationType == NotificationType.SMS) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.SMS_NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.SMS_NOTIFICATION_ROUTING_KEY, notification);
        }

        return notification;

    }

    @Override
    public Notification updateNotification(
            Long notificationId,
            NotificationStatus notificationStatus) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (!notification.isPresent()) {
            throw new RuntimeException("Notification not found with id: " + notificationId);
        }
        notification.get().setStatus(notificationStatus);
        return notificationRepository.save(notification.get());
    }

}
