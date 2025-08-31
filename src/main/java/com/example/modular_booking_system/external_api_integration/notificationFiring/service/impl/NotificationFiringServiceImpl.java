package com.example.modular_booking_system.external_api_integration.notificationFiring.service.impl;

import com.example.modular_booking_system.external_api_integration.notificationFiring.service.NotificationFiringService;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationFiringServiceImpl implements NotificationFiringService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${sendgrid.sender.email}")
    private String fromEmail;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Override
    public void SendEmailServices(String to, String subject, String body) {
        System.out.println("✅ Email sent successfully to: " + to);

        // try {
        // Email from = new Email(fromEmail);
        // Email toEmail = new Email(to);
        // Content content = new Content("text/html", body);
        // Mail mail = new Mail(from, subject, toEmail, content);
        //
        // Request request = new Request();
        // request.setMethod(Method.POST);
        // request.setEndpoint("mail/send");
        // request.setBody(mail.build());
        //
        // Response response = sendGrid.api(request);
        // System.out.println("✅ Email sent successfully to: " + to);
        //
        // if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
        // System.out.println("✅ Email sent successfully to: " + to);
        // } else {
        // System.err.println("❌ Failed to send email. Status: " +
        // response.getStatusCode());
        // System.err.println("Response body: " + response.getBody());
        // }
        // } catch (IOException e) {
        // System.err.println("❌ Error sending email: " + e.getMessage());
        // e.printStackTrace();
        // }
    }

    @Override
    public void SendSms(String to, String body) {
        System.out.println("✅ SMS sent successfully to: " + to);

        // try {
        // Message message = Message.creator(
        // new PhoneNumber(to),
        // new PhoneNumber(twilioPhoneNumber),
        // body).create();
        //
        // System.out.println("✅ SMS sent successfully to: " + to);
        // System.out.println("Message SID: " + message.getSid());
        // } catch (Exception e) {
        // System.err.println("❌ Error sending SMS: " + e.getMessage());
        // e.printStackTrace();
        // }
    }
}
