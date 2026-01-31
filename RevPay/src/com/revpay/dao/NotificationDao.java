package com.revpay.dao;

import java.util.List;
import com.revpay.model.Notification;

public interface NotificationDao {

    boolean createNotification(int userId, String message);

    List<Notification> getNotifications(int userId);

    boolean markAsRead(int notifId);
}
