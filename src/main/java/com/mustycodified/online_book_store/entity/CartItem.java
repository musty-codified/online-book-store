package com.mustycodified.online_book_store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem extends Base{

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Book book;

}
