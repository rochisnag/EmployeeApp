package com.employee.dto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.employee.enums.Roles;

public class EmpModel {
	private String id;
	private String name;
	private String dept;
	private String dob;
	private String address;
	private String email;
	private List<String> roles;
	private String password;

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

	public List<String> getRoles() {
		return roles;
	}

	public String getPassword() {
		return password;
	}

	public void setDept(String dept) {
		if (dept == null || dept.trim().isEmpty()) {
			throw new IllegalArgumentException("Dept cannot be null or empty");
		}
		this.dept = dept;
	}

	public void setDob(String dob) {
		String d[] = dob.split("-");
		int date = Integer.parseInt(d[0]);
		int month = Integer.parseInt(d[1]);
		int year = Integer.parseInt(d[2]);
		if ((date > 31) || (month > 12) || (year > 2007 || year < 1970)) {
			throw new IllegalArgumentException("Invalid dob");
		}
		this.dob = dob;
	}

	public void setAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			throw new IllegalArgumentException("Address cannot be null or empty");
		}
		this.address = address;
	}

	public void setEmail(String email) {
		Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
		Matcher matcher = emailPattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("invalid Email");
		}
		this.email = email;
	}

	public void setPassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("password cannot be null or empty");
		}
		this.password = password;
	}
}
