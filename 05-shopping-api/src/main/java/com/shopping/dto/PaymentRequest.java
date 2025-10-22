package com.shopping.dto;

import com.shopping.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod method;
    
    @Size(max = 500, message = "Payment details must not exceed 500 characters")
    private String paymentDetails;
}

