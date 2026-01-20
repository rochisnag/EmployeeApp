package com.employee.dao;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import java.util.List;
public interface EmployeeDao {
	void addEmployee(String name, String dept, String DOB, String address, String email, List<Roles> rolesArray,
			String hashPassword);
	void updateEmployee(String id, String name, String dept, String DOB, String address, String email,Roles role);
	void deleteEmployee(String id);
	void viewAllEmployee();
	void viewEmployeeById(String id);
	void changePassword(String id, String oldpass, String password);
	void resetPassword(String id, String password);
	void grantRole(String id, String role);
	void revokeRole(String id, String role);
	LoginResult validateUser(String id,String password);
}
 