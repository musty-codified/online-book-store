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


//    @JsonCreator
//    public static Country create(String value) throws ValidationException {
//        if (value == null || value.isEmpty()){
//            return null;
//        }
//        for (Country c : values()){
//            if(value.equals(c.getCountryCode())){
//                return c;
//            }
//        }
//        return null;
//    }

    public static PaymentMethod fromCode(String paymentMethod) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getPaymentMethod().equals(paymentMethod)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown Payment method: " + paymentMethod);
    }
}
