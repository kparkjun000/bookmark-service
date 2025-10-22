package com.shopping.controller;

import com.shopping.dto.PaymentRequest;
import com.shopping.dto.PaymentResponse;
import com.shopping.enums.PaymentStatus;
import com.shopping.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve a payment by its ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Retrieve a payment by order ID")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @PostMapping
    @Operation(summary = "Create payment", description = "Create a new payment for an order")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "Process payment", description = "Process a pending payment")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.processPayment(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status", description = "Update the status of a payment")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund payment", description = "Refund a completed payment")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}

