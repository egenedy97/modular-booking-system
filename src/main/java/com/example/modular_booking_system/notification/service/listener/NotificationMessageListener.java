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
     * Pulls and processes email notification messages
     */
    @RabbitListener(id = "emailListener", queues = RabbitMQConfig.EMAIL_NOTIFICATION_DELAY_QUEUE)
    @Override
    public void handleEmailNotificationMessage(Notification notification) {
        log.info("📧 Received email notification for user: {}", notification.getUser().getContact().getEmail());

        try {
            notificationService.updateNotification(notification.getId(), NotificationStatus.PROCESSING);

            String toEmail = notification.getUser().getContact().getEmail();
            String subject = generateEmailSubject(notification);
            String body = notification.getMessage();

            notificationFiringService.SendEmailServices(toEmail, subject, body);

            notificationService.updateNotification(notification.getId(), NotificationStatus.SUCCESS);
            log.info("✅ Email sent successfully (ID: {})", notification.getId());

        } catch (Exception e) {
            log.error("❌ Failed to send email notification (ID: {})", notification.getId(), e);
            notificationService.updateNotification(notification.getId(), NotificationStatus.FAILED);
        }
    }

    /**
     * Pulls and processes SMS notification messages
     */
    @RabbitListener(id = "smsListener", queues = RabbitMQConfig.SMS_NOTIFICATION_DELAY_QUEUE)
    @Override
    public void handleSmsNotificationMessage(Notification notification) {
        log.info("📱 Received SMS notification for user: {}",
                notification.getUser().getContact().getPhones().isEmpty() ? "No phone"
                        : notification.getUser().getContact().getPhones().get(0).getNumber());

        try {
            notificationService.updateNotification(notification.getId(), NotificationStatus.PROCESSING);

            String toPhoneNumber = getPrimaryPhoneNumber(notification.getUser());
            String message = notification.getMessage();

            if (toPhoneNumber == null || toPhoneNumber.isEmpty()) {
                throw new RuntimeException("User has no phone number configured");
            }

            notificationFiringService.SendSms(toPhoneNumber, message);

            notificationService.updateNotification(notification.getId(), NotificationStatus.SUCCESS);
            log.info("✅ SMS sent successfully (ID: {})", notification.getId());

        } catch (Exception e) {
            log.error("❌ Failed to send SMS notification (ID: {})", notification.getId(), e);
            notificationService.updateNotification(notification.getId(), NotificationStatus.FAILED);
        }
    }

    /**
     * Helper: pause listener if processed 1000 messages
     */
    private String getPrimaryPhoneNumber(User user) {
        if (user.getContact() == null || user.getContact().getPhones().isEmpty()) {
            return null;
        }
        Phone primaryPhone = user.getContact().getPhones().get(0);
        return primaryPhone.getCountryCallingCode() + primaryPhone.getNumber();
    }

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
