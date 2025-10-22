package com.shopping.service;

import com.shopping.dto.PaymentRequest;
import com.shopping.dto.PaymentResponse;
import com.shopping.entity.Order;
import com.shopping.entity.Payment;
import com.shopping.enums.OrderStatus;
import com.shopping.enums.PaymentStatus;
import com.shopping.exception.BadRequestException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));
        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot create payment for cancelled order");
        }

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this order");
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .transactionId(generateTransactionId())
                .paymentDetails(request.getPaymentDetails())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse processPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new BadRequestException("Payment is already completed");
        }

        if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new BadRequestException("Cannot process failed payment. Create a new payment.");
        }

        // Simulate payment processing
        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        // In real scenario, this would interact with payment gateway
        // For now, we'll mark it as completed
        payment.setStatus(PaymentStatus.COMPLETED);

        // Update order status
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        payment.setStatus(status);

        // Update order status based on payment status
        if (status == PaymentStatus.COMPLETED) {
            payment.getOrder().setStatus(OrderStatus.CONFIRMED);
        } else if (status == PaymentStatus.FAILED) {
            payment.getOrder().setStatus(OrderStatus.PENDING);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BadRequestException("Can only refund completed payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        // Update order status
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);

        Payment refundedPayment = paymentRepository.save(payment);
        return mapToResponse(refundedPayment);
    }

    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        long random = (long) (Math.random() * 10000);
        return "TXN" + timestamp + String.format("%04d", random);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paymentDetails(payment.getPaymentDetails())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}

