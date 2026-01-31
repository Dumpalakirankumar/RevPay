package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.revpay.exception.InvalidAmountException;

public class InvalidAmountExceptionTest {

    @Test
    public void testExceptionMessage() {
        InvalidAmountException ex =
                new InvalidAmountException("Invalid amount");

        assertEquals("Invalid amount", ex.getMessage());
    }
}


