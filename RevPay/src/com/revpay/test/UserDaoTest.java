package com.revpay.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revpay.dao.UserDao;
import com.revpay.dao.impl.UserDaoImpl;
import com.revpay.exception.EmailAlreadyExistsException;
import com.revpay.model.User;

public class UserDaoTest {

    private UserDao userDao = new UserDaoImpl();

    @Test
    public void testDuplicateEmailRegistration() throws Exception {
        UserDao userDao = new UserDaoImpl();

        User user = new User();
        user.setFullName("Test User");
        user.setEmail("dumpalakirankumar1009@gmail.com"); // already exists
        user.setPhone("9999999999");
        user.setPassword("123456");
        user.setAccountType("PERSONAL");

        try {
            userDao.registerUser(user);
            fail("Expected EmailAlreadyExistsException");
        } catch (EmailAlreadyExistsException e) {
            assertEquals("Email already registered. Please login.", e.getMessage());
        }
    }
}
