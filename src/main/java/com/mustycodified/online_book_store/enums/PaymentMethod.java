package com.mustycodified.online_book_store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    WEB("WEB"),
    USSD("USSD"),
    TRANSFER("TRANSFER");
    private final String paymentMethod;

    public static PaymentMethod fromPaymentMethod(String paymentMethod) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getPaymentMethod().equals(paymentMethod)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown Payment method: " + paymentMethod);
    }
}
