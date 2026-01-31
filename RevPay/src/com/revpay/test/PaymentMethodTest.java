package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.revpay.model.PaymentMethod;

public class PaymentMethodTest {

    @Test
    public void testCardDetails() {
        PaymentMethod card = new PaymentMethod();

        card.setCardType("DEBIT");
        card.setExpiryYear(2028);

        assertEquals("DEBIT", card.getCardType());
        assertEquals(2028, card.getExpiryYear());
    }
}

