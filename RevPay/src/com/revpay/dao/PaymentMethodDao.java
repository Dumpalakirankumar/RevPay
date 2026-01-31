package com.revpay.dao;

import java.util.List;
import com.revpay.model.PaymentMethod;

public interface PaymentMethodDao {

    boolean addCard(PaymentMethod card);

    List<PaymentMethod> getCardsByUser(int userId);

    boolean setDefaultCard(int userId, int cardId);
}
