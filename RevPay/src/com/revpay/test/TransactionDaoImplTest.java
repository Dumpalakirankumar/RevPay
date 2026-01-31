package com.revpay.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.revpay.dao.TransactionDao;
import com.revpay.dao.impl.TransactionDaoImpl;
import com.revpay.exception.InvalidAmountException;

public class TransactionDaoImplTest {

    private TransactionDao transactionDao;

    @Before
    public void setUp() {
        transactionDao = new TransactionDaoImpl();
    }

    // ✅ Valid Transfer
    @Test
    public void testSendMoneySuccess() {
        boolean result = transactionDao.sendMoney(
                1,     // senderId (exists)
                2,     // receiverId (exists)
                100.0,
                "JUnit transfer"
        );

        assertTrue(result);
    }

    // ❌ Invalid Amount
    @Test(expected = InvalidAmountException.class)
    public void testSendMoneyInvalidAmount() {
        transactionDao.sendMoney(
            1,
            2,
            -50.0,
            "Invalid amount"
        );
    }
}


