package com.example.modular_booking_system.notification.service.listener;

import com.example.modular_booking_system.core.config.RabbitMQConfig;
import com.example.modular_booking_system.external_api_integration.notificationFiring.service.NotificationFiringService;
import com.example.modular_booking_system.notification.model.Notification;
import com.example.modular_booking_system.notification.model.NotificationStatus;

import com.example.modular_booking_system.notification.service.NotificationListener;
import com.example.modular_booking_system.notification.service.NotificationService;
import com.example.modular_booking_system.user.model.User;
import com.example.modular_booking_system.user.model.Phone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationMessageListener implements NotificationListener {

    private final NotificationFiringService notificationFiringService;
    private final NotificationService notificationService;

    /**
     * Pulls and processes email notification messages from the RabbitMQ queue
     * This implements the pull strategy for email notifications
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_NOTIFICATION_QUEUE)
    @Override
    public void handleEmailNotificationMessage(Notification notification) {
        log.info("📧 Received email notification message for user: {}", notification.getUser().getContact().getEmail());

        try {
            // Update status to processing
            notificationService.updateNotification(notification.getId(), NotificationStatus.PROCESSING);

            // Extract email details
            String toEmail = notification.getUser().getContact().getEmail();
            String subject = generateEmailSubject(notification);
            String body = notification.getMessage();

            // Send email using NotificationFiringService
            notificationFiringService.SendEmailServices(toEmail, subject, body);

            // Update status to sent
            notificationService.updateNotification(notification.getId(), NotificationStatus.SUCCESS);
            log.info("✅ Email notification sent successfully for notification ID: {}", notification.getId());

        } catch (Exception e) {
            log.error("❌ Failed to send email notification for ID: {}", notification.getId(), e);

            // Update status to failed
            notificationService.updateNotification(notification.getId(), NotificationStatus.FAILED);
        }
    }

    /**
     * Pulls and processes SMS notification messages from the RabbitMQ queue
     * This implements the pull strategy for SMS notifications
     */
    @RabbitListener(queues = RabbitMQConfig.SMS_NOTIFICATION_QUEUE)
    @Override
    public void handleSmsNotificationMessage(Notification notification) {
        log.info("📱 Received SMS notification message for user: {}",
                notification.getUser().getContact().getPhones().isEmpty() ? "No phone"
                        : notification.getUser().getContact().getPhones().get(0).getNumber());

        try {
            // Update status to processing
            notificationService.updateNotification(notification.getId(), NotificationStatus.PROCESSING);

            // Extract SMS details - get primary phone number
            String toPhoneNumber = getPrimaryPhoneNumber(notification.getUser());
            String message = notification.getMessage();

            if (toPhoneNumber == null || toPhoneNumber.isEmpty()) {
                throw new RuntimeException("User has no phone number configured");
            }

            // Send SMS using NotificationFiringService
            notificationFiringService.SendSms(toPhoneNumber, message);

            // Update status to sent
            notificationService.updateNotification(notification.getId(), NotificationStatus.SUCCESS);
            log.info("✅ SMS notification sent successfully for notification ID: {}", notification.getId());

        } catch (Exception e) {
            log.error("❌ Failed to send SMS notification for ID: {}", notification.getId(), e);

            // Update status to failed
            notificationService.updateNotification(notification.getId(), NotificationStatus.FAILED);
        }
    }

    /**
     * Gets the primary phone number for a user
     * Returns the first available phone number or constructs full number with
     * country code
     */
    private String getPrimaryPhoneNumber(User user) {
        if (user.getContact() == null || user.getContact().getPhones().isEmpty()) {
            return null;
        }

        Phone primaryPhone = user.getContact().getPhones().get(0);
        return primaryPhone.getCountryCallingCode() + primaryPhone.getNumber();
    }

    /**
     * Generates an appropriate email subject based on notification type and content
     */
    private String generateEmailSubject(Notification notification) {
        if (notification.getMessage().toLowerCase().contains("booking")) {
            return "Booking Confirmation - Modular Booking System";
        } else if (notification.getMessage().toLowerCase().contains("payment")) {
            return "Payment Confirmation - Modular Booking System";
        } else if (notification.getMessage().toLowerCase().contains("flight")) {
            return "Flight Update - Modular Booking System";
        } else {
            return "Notification - Modular Booking System";
        }
    }
}
