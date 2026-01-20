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