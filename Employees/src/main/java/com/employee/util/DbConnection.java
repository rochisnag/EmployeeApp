package com.employee.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
	public static  Connection DBConnection() {
		String url = "jdbc:postgresql://localhost:5432/employee";
		String username = "postgres";
		String password = "root";
		try {
		Connection con = DriverManager.getConnection(url, username, password);
		System.out.println("Connected!!");
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
