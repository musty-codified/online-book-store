package com.mustycodified.online_book_store.service.impl;

import com.mustycodified.online_book_store.entity.Order;
import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.enums.OrderStatus;
import com.mustycodified.online_book_store.enums.PaymentStatus;
import com.mustycodified.online_book_store.repository.OrderRepository;
import com.mustycodified.online_book_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void processPayment(Order order) {
        try {
            log.info("processing payment asynchronously for order:{}, Thread:{}", order.getId(), Thread.currentThread().getName());
            Thread.sleep(5000); // Simulate payment delay

            User user = userRepository.findById(order.getUser().getId()).orElseThrow();
            BigDecimal balance = user.getWalletBalance();
            BigDecimal total = BigDecimal.valueOf(order.getGrandTotal());

            if (balance.compareTo(total) < 0) {
                order.setOrderStatus(OrderStatus.FAILED.toString());
                order.setPaymentStatus(PaymentStatus.FAILED);
                log.warn("Payment failed for order {}: insufficient balance", order.getId());
            } else {
                user.setWalletBalance(balance.subtract(total));
                userRepository.save(user);
                order.setOrderStatus(OrderStatus.COMPLETED.toString());
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                log.info("Payment successful for order: {}", order.getId());
            }
            orderRepository.save(order);
            log.info("{} Payment has been recorded for user ID :{}", order.getOrderStatus(), user.getId());

        } catch (Exception e) {
            order.setOrderStatus(OrderStatus.FAILED.toString());
            orderRepository.save(order);
            log.error("Unexpected error during payment processing for order {}: {}", order.getId(), e.getMessage());

        }
          order.setTransactionReference(UUID.randomUUID().toString());
    }
}
