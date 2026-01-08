package com.employee.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.employee.model.Employee;

public class EmployeeUtil {
	Employee employee = new Employee();
	public String hash(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error hashing password");
			return "";
		}
	}	
}
