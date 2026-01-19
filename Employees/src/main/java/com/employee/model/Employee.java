package com.employee.model;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.employee.util.Roles;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String dob;
	private String address;
	private String email;
	private String role;
	private String password;
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDept() {
		return dept;
	}
	public String getDob() {
		return dob;
	}
	public String getAddress() {
		return address;
	}
	public String getEmail() {
		return email;
	}
	public String getRole() {
		return role;
	}
	public String getPassword() {
		return password;
	}
	public void setName(String name) {
		this.name = name.trim();
	}
	public void setId(String id) {
		this.id = id.trim().toUpperCase();
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public void setDob(String dob) {
		if (dob == null || !dob.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			throw new IllegalArgumentException("Invalid DOB format. Expected: DD-MM-YYYY");
		}
		String[] parts = dob.split("-");
		int date = Integer.parseInt(parts[2]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[0]);
		int minBirthYear = year - 100; 
		int maxBirthYear = year - 18;
		if (year <minBirthYear || year > maxBirthYear || month < 1 || month > 12) {
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
		this.dob = dob;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}