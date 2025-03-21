package com.mustycodified.online_book_store.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mustycodified.online_book_store.enums.PaymentMethod;
import com.mustycodified.online_book_store.util.PaymentMethodDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    @JsonDeserialize(using = PaymentMethodDeserializer.class)
    private PaymentMethod paymentMethod;

}
