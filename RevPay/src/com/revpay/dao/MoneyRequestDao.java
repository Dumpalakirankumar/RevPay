package com.revpay.dao;

import java.util.List;
import com.revpay.model.MoneyRequest;

public interface MoneyRequestDao {

    boolean createRequest(MoneyRequest request);

    List<MoneyRequest> getPendingRequests(int userId);

    boolean updateRequestStatus(int requestId, String status);
}

