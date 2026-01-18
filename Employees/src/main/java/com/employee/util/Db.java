package com.employee.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Db {
	public static  Connection getConnection() {
		String url = "jdbc:postgresql://localhost:5432/employee";
		String username = "postgres";
		String password = "root";
		try {
		Connection con = DriverManager.getConnection(url, username, password);
		System.out.println("Connected");
			return con;
		} catch (Exception e) {
			System.out.println("Error :"+e.getMessage());
		}
		return null;
	}
}
