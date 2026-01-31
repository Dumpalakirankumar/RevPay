package com.revpay.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revpay.model.User;

public class UserServiceTest {

    @Test
    public void testUserObjectCreation() {
        User user = new User();

        user.setUserId(1);
        user.setFullName("JUnit User");
        user.setEmail("junit@test.com");
        user.setAccountType("PERSONAL");

        assertEquals("JUnit User", user.getFullName());
        assertEquals("PERSONAL", user.getAccountType());
    }
}
