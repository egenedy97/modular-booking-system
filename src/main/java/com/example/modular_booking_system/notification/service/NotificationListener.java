package com.example.modular_booking_system.notification.service;

import com.example.modular_booking_system.notification.model.Notification;

/**
 * Interface for notification message listeners that implement the pull strategy
 * for processing notification messages from RabbitMQ queues
 */
public interface NotificationListener {

    /**
     * Handles email notification messages pulled from the RabbitMQ email queue
     * 
     * @param notification The notification object containing email details and user
     *                     information
     */
    void handleEmailNotificationMessage(Notification notification);

    /**
     * Handles SMS notification messages pulled from the RabbitMQ SMS queue
     * 
     * @param notification The notification object containing SMS details and user
     *                     information
     */
    void handleSmsNotificationMessage(Notification notification);
}
