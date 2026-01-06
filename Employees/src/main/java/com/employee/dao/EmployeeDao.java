package com.employee.dao;

import com.google.gson.JsonArray;
import com.employee.model.Employee;

public interface EmployeeDao {
	void addEmployee(String name, String dept, String DOB, String address, String email, JsonArray rolesArray,
			String hashPassword);
	void updateEmployee(String id, String name, String dept, String DOB, String address, String email);
	void deleteEmployee(String id);
	void viewEmployee();
	void viewEmployee_by_id(String id);
	void changePassword(String id, String oldpass, String password);
	void resetPassword(String id, String password);
	void grantRole(String id, String role);
	void revokeRole(String id, String role);
	Employee getEmployeeById(String id);
}
