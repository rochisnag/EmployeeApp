package com.employee.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.employee.util.Roles;

public class Employee {
	private String id;
	private String fname;
	private String lname;
	private String dept;
	private String dob;
	private String address;
	private String email;
	private String role;
	private String password;

	public String getId() {
		return id;
	}

	public String getFname() {
		return fname;
	}
	public String getLname() {
		return lname;
	}

	public String getDept() {
		return dept;
	}

	public String getDOB() {
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
	  public void setFname(String fname) {
	        this.fname = fname;
	    }
	  public void setLname(String lname) {
	        this.lname = lname;
	    }
	public void setId(String id) {
		Pattern idPattern = Pattern.compile("tek\\d{1,}");
		Matcher matcher = idPattern.matcher(id);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid Id");
		}
		this.id = id;
	}

	
	public void setDept(String dept) {
		if (dept == null || dept.trim().isEmpty()) {
			throw new IllegalArgumentException("Dept cannot be null or empty");
		}
		this.dept = dept;
	}

	public void setDOB(String dob) {
		String j[] = dob.split("-");
		int date = Integer.parseInt(j[0]);
		int month = Integer.parseInt(j[1]);
		int year = Integer.parseInt(j[2]);
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
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.[A-za-z]{2,}");
		Matcher matcher = emailPattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("invalid Email");
		}
		this.email = email;
	}

	public void setRole(String role) {
		try {
			Roles choice;
			choice = Roles.valueOf(role.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid Role");
		}
		this.role = role;
	}

	public void setPassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("password cannot be null or empty");
		}
		this.password = password;
	}
}