package com.employee.dao;

import java.security.MessageDigest;

public class HashPassword {
	public static String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error hashing password");
		}
	}
}
