package com.employee.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import org.mindrot.jbcrypt.BCrypt;
import com.employee.model.Employee;
public class EmployeeUtil {
 Employee employee = new Employee();
   public String hash(String password) {
	    return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	public boolean verify(String plainPassword, String hashedPassword) {  
		 if (plainPassword == null) {
		        throw new NullPointerException("Password cannot be null");
		    }
		    if (plainPassword.isEmpty()) {
		        return false;
		    }return BCrypt.checkpw(plainPassword, hashedPassword);
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
	
	public boolean validateId(String id) {
	    if (id == null || id.trim().isEmpty()) {
	        System.out.println("ID cannot be null or empty");
	          return false;
	    }
		Pattern idPattern = Pattern.compile("TEK-\\d+");
		Matcher matcher = idPattern.matcher(id);
		if (matcher.matches()) {
			employee.setId(id);
			return true;
		}
		System.out.println("Invalid ID format");
		return false;
	}
	public boolean validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			System.out.println("Invalid Name format");
			return false;
		}
		employee.setName(name);
		return true;
	}
	public boolean validateDept(String dept) {
		if (dept == null || dept.trim().isEmpty()) {
			System.out.println("Invalid Department format");
			return false;
		}
		employee.setDept(dept);
		return true;
	}
	public boolean validateDob(String dob) {
		 if (dob == null || !dob.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
		        throw new IllegalArgumentException("Invalid DOB format. Expected: YYYY-M-D");
		    }
	    String[] parts = dob.split("-");
	    int date = Integer.parseInt(parts[2]);
	    int month = Integer.parseInt(parts[1]);
	    int year = Integer.parseInt(parts[0]);
	    int currentYear = java.time.LocalDate.now().getYear();
	    int minBirthYear = currentYear - 100; 
	    int maxBirthYear = currentYear - 18;
	    if (year < minBirthYear || year > maxBirthYear || month < 1 || month > 12) {
	        throw new IllegalArgumentException("Invalid dates");
	    }
	    int maxDays;
	    if (month == 2) {
	        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
	            maxDays = 29;
	        } else {
	            maxDays = 28;
	        }
	    } else if (month == 4 || month == 6 || month == 9 || month == 11) {
	        maxDays = 30;
	    } else {
	        maxDays = 31;
	    }
	    if (date < 1 || date > maxDays) {
	        throw new IllegalArgumentException("Invalid dates");
	    }
	    employee.setDob(dob);
	    return true;
	}

	public boolean validateAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			System.out.println("Invalid address format");
			return false;
		}
		employee.setDept(address);
		return true;
	}
	public boolean validateEmail(String email) {
	    if (email == null || email.trim().isEmpty()) {
	        System.out.println("ID cannot be null or empty");
	        return false;
	    }
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.[A-za-z]{2,}");
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.matches()) {
			employee.setEmail(email);
			return true;
		}
		System.out.println("Invalid Email format");
		return false;
	}
	public boolean validatePassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			System.out.println("Invalid password format");
			return false;
		}
		employee.setPassword(password);
		return true;
	}
	public Roles validateRole(String role) {
	    if (role == null || role.trim().isEmpty()) {
	        throw new IllegalArgumentException("Role cannot be empty");
	    }
	    try {
	        return Roles.valueOf(role.trim().toUpperCase());
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Invalid role: " + role);
	    }
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
