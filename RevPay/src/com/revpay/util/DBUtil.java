package com.revpay.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	private static final String URL =
		    "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "revpay";
    private static final String PASSWORD = "revpay";

    private DBUtil() {
        // Prevent object creation
    }

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
