package com.example.modular_booking_system.payment.dto.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayPalOrderResponse {
    private String id;
    private String status;
    private List<Link> links;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Link {
        private String href;
        private String rel;
        private String method;
    }
    
    public String getApprovalLink() {
        return links.stream()
                .filter(link -> "approve".equals(link.rel))
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }
}
