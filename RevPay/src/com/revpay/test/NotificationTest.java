package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.revpay.model.Notification;

public class NotificationTest {

    @Test
    public void testNotificationMessage() {
        Notification n = new Notification();
        n.setMessage("You received ₹200");

        assertEquals("You received ₹200", n.getMessage());
    }
}

