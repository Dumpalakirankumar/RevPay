package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.revpay.model.User;

public class UserTest {

    @Test
    public void testUserObjectCreation() {
        User user = new User();

        user.setFullName("JUnit User");
        user.setEmail("junit@gmail.com");
        user.setAccountType("PERSONAL");

        assertEquals("JUnit User", user.getFullName());
        assertEquals("junit@gmail.com", user.getEmail());
        assertEquals("PERSONAL", user.getAccountType());
    }
}

