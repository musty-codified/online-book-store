package com.mustycodified.online_book_store.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mustycodified.online_book_store.enums.PaymentMethod;

import java.io.IOException;

public class PaymentMethodDeserializer extends JsonDeserializer<PaymentMethod> {
    @Override
    public PaymentMethod deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String code = jsonParser.getText();
        return PaymentMethod.fromCode(code);

    }
}
