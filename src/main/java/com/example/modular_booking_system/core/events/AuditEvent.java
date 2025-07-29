package com.example.modular_booking_system.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    private String messageId;
    private String action;
    private String serviceName;
    private String topicName;
    private Object data;
    private String auditBy;
    private LocalDateTime auditDate;

}