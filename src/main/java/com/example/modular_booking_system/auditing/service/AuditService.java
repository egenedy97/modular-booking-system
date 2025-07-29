package com.example.modular_booking_system.auditing.service;

import com.example.modular_booking_system.auditing.model.AuditLog;
import com.example.modular_booking_system.auditing.repository.AuditLogRepository;
import com.example.modular_booking_system.core.events.AuditEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void saveAuditLog(AuditEvent event) {

        JsonNode jsonData = objectMapper.valueToTree(event.getData());

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(event.getAction());
        auditLog.setMessageId(event.getMessageId());
        auditLog.setServiceName(event.getServiceName());
        auditLog.setTopicName(event.getTopicName());
        auditLog.setAuditBy(event.getAuditBy());
        auditLog.setData(jsonData);
        auditLog.setAuditDate(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }
}
