package com.employee.util;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import org.mindrot.jbcrypt.BCrypt;
import com.employee.model.Employee;

public class EmployeeUtil {
	private Employee employee = new Employee();
	public static  String hash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	public static  boolean verify(String plainPassword, String hashedPassword) {
		if (plainPassword == null) {
			throw new NullPointerException("Password cannot be null");
		}
		if (plainPassword.isEmpty()) {
			return false;
		}
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
	public static String generateRandomPassword() {
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
			return false;
		}
		Pattern idPattern = Pattern.compile("TEK-\\d+");
		Matcher matcher = idPattern.matcher(id);
		if (matcher.matches()) {
			employee.setId(id);
			return true;
		}
		return false;
	}

	public boolean validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			System.out.println("Invalid Name format");
			return false;
		}
		if (!name.matches("[a-zA-Z ]+")) {
			System.out.println("Name must contain only letters");
			return false;
		}
		employee.setName(name);
		return true;
	}

	public boolean validateDept(String dept) {
		if (dept == null || dept.trim().isEmpty()) {
			return false;
		}
		if (!dept.matches("[a-zA-Z ]+")) {
			System.out.println("Dept must contain only letters");
			return false;
		}
		employee.setDept(dept);
		return true;
	}

	public boolean validateDob(String dob) {
		if (dob == null || !dob.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			return false;
		}
		String[] parts = dob.split("-");
		int date = Integer.parseInt(parts[2]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[0]);
		int currentYear = java.time.LocalDate.now().getYear();
		int minBirthYear = currentYear - 100;
		int maxBirthYear = currentYear - 18;
		if (year < minBirthYear || year > maxBirthYear || month < 1 || month > 12) {
			return false;
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
			return false;
		}
		employee.setDob(dob);
		return true;
	}

	public boolean validateAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			return false;
		}
		if (!address.matches("[a-zA-Z ]+")) {
			System.out.println("addresss must contain only letters");
			return false;
		}
		employee.setAddress(address);
		return true;
	}

	public boolean validateEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.[A-za-z]{2,}");
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.matches()) {
			employee.setEmail(email);
			return true;
		}
		return false;
	}
	public boolean validatePassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			return false;
		}
		Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=]).{8,}$");
		Matcher matcher = passwordPattern.matcher(password);
		if (!matcher.matches()) {
			return false;
		}
		employee.setPassword(hash(password));
		return true;
	}

	public boolean validateRole(List<Roles> roles) {
		if (roles == null || roles.isEmpty()) {
		
			return false;
		}
		List<Roles> normalizedRoles = new ArrayList<>();
		for (Roles role : roles) {
			if (role == null) {
				return false;
			}
			normalizedRoles.add(Roles.valueOf(role.toString().toUpperCase()));
		}
		employee.setRole(new ArrayList<>(normalizedRoles));
		return true;
	}

	public boolean validateSingleRole(String role) {
		if (role == null) {
			return false;
		}
		try {
			Roles.valueOf(role.toString().toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
