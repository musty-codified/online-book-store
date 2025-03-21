package com.mustycodified.online_book_store.entity;


import com.mustycodified.online_book_store.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends Base{

    @Column(nullable = false)
    private Double grandTotal;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String orderStatus;

}
