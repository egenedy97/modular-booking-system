package com.example.modular_booking_system.external_api_integration.notificationFiring.service;

public interface NotificationFiringService {
    void SendEmailServices(String to, String subject, String body);
    void SendSms(String to, String body);
}
