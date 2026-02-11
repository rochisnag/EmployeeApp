package com.employee.dao;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import com.employee.model.Employee;
import java.util.List;

public interface EmployeeDao {
	void addEmployee(Employee e) ;
    boolean updateEmployee(Employee e,Roles role);
    boolean deleteEmployee(String id);
	List<Employee> viewAllEmployee();
	Employee viewEmployeeById(String id);
    boolean changePassword(String id, String oldpass, String password);
	boolean resetPassword(String id, String password);
    boolean grantRole(String id, Roles role) ;
	boolean revokeRole(String id, Roles role);
	List<Employee> fetchInActiveEmployees();
	LoginResult validateUser(String id,String password);
}
