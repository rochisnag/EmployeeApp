package com.employee.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import org.mindrot.jbcrypt.BCrypt;
public class EmployeeUtil {
	public String hash(String password) {
	    return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	public boolean verify(String plainPassword, String hashedPassword) {
	    return BCrypt.checkpw(plainPassword, hashedPassword);
	}
	public  String generateRandomPassword() {
		String lower = "abcdefghijklmnopqrstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String specialChar = "!@#$%_*+";
		String digits = "0123456789";
		String allChars = lower + upper + specialChar + digits;
		SecureRandom sr = new SecureRandom();
		List<Character> passwordCharList = new ArrayList<>();
		passwordCharList.add(lower.charAt(sr.nextInt(lower.length())));
		passwordCharList.add(upper.charAt(sr.nextInt(upper.length())));
		passwordCharList.add(specialChar.charAt(sr.nextInt(specialChar.length())));
		passwordCharList.add(digits.charAt(sr.nextInt(digits.length())));
		for (int index = 0; index < 4; index++) {
			passwordCharList.add(allChars.charAt(sr.nextInt(allChars.length())));
		}
		Collections.shuffle(passwordCharList);
		StringBuilder password = new StringBuilder();
		for (char c : passwordCharList) {
			password.append(c);
		}
		return password.toString();
	}
	public  Connection getConnection() {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("src/main/resources/DbConfig.properties")) {
			prop.load(input);
	     	String url = prop.getProperty("db.url");
			String username = prop.getProperty("db.username");
			String password = prop.getProperty("db.password");
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection to db successful...");
			return conn;
		} catch (IOException e) {
			System.out.println("Unable to read property file" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Unable to connect to DB" + e.getMessage());
		}
		return null;
	}
	
}
