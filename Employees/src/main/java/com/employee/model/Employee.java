package com.employee.model;
import java.util.List;
import java.util.ArrayList;
import com.employee.util.Roles;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String dob;
	private String address;
	private String email;
	private List<Roles> roles = new ArrayList<>(); 
	private String password;
	public Employee() {}
	public Employee(String name, String dept, String dob, String address, String email, List<Roles> roles, String password){
		this.name = name;
		this.dept = dept;
		this.dob = dob;
		this.address = address;
		this.email = email;
		this.roles = roles;
		this.password =password;
	}
	public Employee(String id, String name, String dept, String dob, String address, String email) {
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.dob = dob;
		this.address = address;
		this.email = email;
	}
	
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
	public List<Roles> getRoles() {
		return roles;
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
	public void setRole(List<Roles> roles) {
		this.roles= roles;
	}
	public void setPassword(String password) {
		this.password = password;
	} 

}