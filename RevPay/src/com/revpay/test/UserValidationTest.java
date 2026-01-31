package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.revpay.exception.*;
import com.revpay.service.UserService;

public class UserValidationTest {

    private UserService userService = new UserService();

    // ❌ Invalid Email
    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail() {
        userService.validateEmail("test@yahoo.com");
    }

    // ❌ Invalid Phone
    @Test(expected = InvalidPhoneException.class)
    public void testInvalidPhone() {
        userService.validatePhone("12345");
    }

    // ❌ Invalid Password
    @Test(expected = InvalidPasswordException.class)
    public void testInvalidPassword() {
        userService.validatePassword("123");
    }

    // ✅ Valid Inputs
    @Test
    public void testValidInputs() {
        userService.validateEmail("test@gmail.com");
        userService.validatePhone("9876543210");
        userService.validatePassword("123456");
    }
}

