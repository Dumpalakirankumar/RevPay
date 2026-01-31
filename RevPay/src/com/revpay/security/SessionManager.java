package com.revpay.security;

public class SessionManager {

    private static final long TIMEOUT_MS = 10 * 60 * 1000; // 10 minutes

    private static long lastActivityTime;

    public static void startSession() {
        lastActivityTime = System.currentTimeMillis();
    }

    public static void updateActivity() {
        lastActivityTime = System.currentTimeMillis();
    }

    public static boolean isSessionExpired() {
        long now = System.currentTimeMillis();
        return (now - lastActivityTime) > TIMEOUT_MS;
    }
}