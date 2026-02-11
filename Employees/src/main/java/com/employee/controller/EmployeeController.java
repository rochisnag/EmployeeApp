package com.employee.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.model.Employee;
import com.employee.services.EmployeeService;
import com.employee.services.LoginService;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;

public class EmployeeController {
	private final Scanner sc = new Scanner(System.in);
	private final EmployeeService employeeService = new EmployeeService();
	private final LoginService loginService = new LoginService();
	public void addEmployee(EmployeeDao dao) {
		System.out.println("Enter emp first name:");
		String fname = sc.nextLine().trim().toUpperCase();
		System.out.println("Enter emp last name:");
		String lname = sc.nextLine().trim().toUpperCase();
		String name = fname + " " + lname;
		System.out.println("Enter emp dept:");
		String dept = sc.nextLine().trim().toUpperCase();
		System.out.println("Enter emp day in DOB:");
		String day = sc.nextLine().trim().toUpperCase();
		System.out.println("Enter emp month in DOB:");
		String month = sc.nextLine().trim().toUpperCase();
		System.out.println("Enter emp year in DOB:");
		String year = sc.nextLine().trim().toUpperCase();
		String dob = year + "-" + month + "-" + day;
		System.out.println("Enter emp address:");
		String address = sc.nextLine().trim().toUpperCase();
		System.out.println("Enter emp email:");
		String email = sc.nextLine().trim().toUpperCase();
		System.out.println("Available Roles:");
		for (Roles role : Roles.values()) {
			System.out.println(role);
		}
		System.out.println("Enter emp roles (comma separated):");
		String roleInput = sc.nextLine().toUpperCase();
		List<Roles> roles = new ArrayList<>();
		for (String roleStr : roleInput.split(",")) {
			if (roleStr != null && !roleStr.trim().isEmpty()) {
				try {
					Roles role = Roles.valueOf(roleStr.trim().toUpperCase());
					roles.add(role);
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid role: " + roleStr);
				}
			}
		}
		String password = EmployeeUtil.generateRandomPassword();
		String hashedPassword = EmployeeUtil.hash(password);
		Employee employee = new Employee(name, dept, dob, address, email, roles, hashedPassword);
		try {
			employeeService.insert(dao, employee);
			System.out.println("Employee added successfully");
			System.out.println("Your temporary password:" + password);
		} catch (ServiceException | ValidationException e) {
			System.out.println(e.getMessage());
		}
	}
	public void grantRole(EmployeeDao dao) {
		System.out.print("Enter Employee ID to grant role: ");
		String empId = sc.nextLine().trim().toUpperCase();
		System.out.print("Enter Role to GRANT: ");
		String roleInput = sc.nextLine().trim().toUpperCase();
		try {
			boolean success = employeeService.grantRole(empId, roleInput, dao);
			if (success) {
				System.out.println("Role granted successfully to employee: " + empId);
				 List<Employee> employees = employeeService.getAllEmployees(dao);	
					for (Employee e : employees) {
						 printEmployee(e);
					}
			}else {
	            System.out.println("Failed to grant role.");
	        }
		} catch (ServiceException | ValidationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void revokeRole(EmployeeDao dao) {
		System.out.print("Enter Employee ID to revoke role: ");
		String empId = sc.nextLine().trim().toUpperCase();
		System.out.print("Enter Role to revoke: ");
		String role = sc.nextLine().trim().toUpperCase();
		try {
			boolean success =employeeService.revokeRole(empId, role, dao);
			if (success) {
				System.out.println("Role revoked successfully for employee: " + empId);
				 List<Employee> employees = employeeService.getAllEmployees(dao);	
					for (Employee e : employees) {
						 printEmployee(e);
					}		
			} else {
				   System.out.println("Failed to revoke role.");
			}
		} catch (ServiceException | ValidationException e) {
			System.out.println(e.getMessage());
		}
	}
	public void delete(EmployeeDao dao) {
		System.out.print("Enter Employee ID to grant role: ");
		String empId = sc.nextLine().trim().toUpperCase();
		try {
		    boolean deleted = employeeService.delete(dao, empId);
	        if (deleted) {
	            System.out.println("Employee deleted successfully: " + empId);
	            List<Employee> employees = employeeService.getAllEmployees(dao);
				for (Employee e : employees) {
					 printEmployee(e);
				}
	        } else {
	        System.out.println("Failed to delete employee");
	        }
	       } catch (ServiceException | ValidationException | EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		}
	}
	public void viewAllEmployees(EmployeeDao dao) {
		try {
			List<Employee> employees =employeeService.getAllEmployees(dao);
			if (employees.isEmpty()) {
				System.out.println("No employees found.");
			} else {
				System.out.println("Employee List:");
				for (Employee e : employees) {
					 printEmployee(e);
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	public void viewEmployeeById(EmployeeDao dao) {
		try {
			System.out.println("Enter emp id:");
			String id = sc.nextLine().toUpperCase();
			Employee employee = employeeService.getEmployeeById(dao, id);
			 printEmployee(employee);
		} catch (ValidationException | ServiceException e) {
			System.out.println(e.getMessage());
		}catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void changePassword(EmployeeDao dao) {
		String id = Menu.currentUser.getEmpId();
		System.out.println("Enter old password:");
		String oldPassword = sc.nextLine().trim();
		System.out.println("Enter new password:");
		String newPassword = sc.nextLine().trim();
		System.out.println("Re-enter new password:");
		String confirmPassword = sc.nextLine().trim();
		if (!newPassword.trim().equals(confirmPassword.trim())) {
		    System.out.println("Passwords didn't match");
		    return;
		}
		try {
			boolean success = loginService.changePassword(dao, id, oldPassword, newPassword);
			if (success) {
				System.out.println("Password changed successfully!");
			} 
			else {
			   System.out.println("Failed to change password.");
			}
		} catch (ValidationException | ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	public void resetPassword(EmployeeDao dao) {
		System.out.print("Enter employee ID to reset password:");
		String id = sc.nextLine().trim().toUpperCase();
		try {
			boolean success = loginService.resetPassword(dao, id);
			if (success) {
				System.out.println("Password reset successfully for employee: " + id);
			} 
			else {
		   System.out.println("Failed to reset password");
			}
		} catch (ValidationException | ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	public void update(EmployeeDao dao) {
		Roles role;
		String empId;
		if (Menu.currentUser.getRoles().contains(Roles.ADMIN)
				|| Menu.currentUser.getRoles().contains(Roles.MANAGER)) {
			role = Roles.ADMIN;
			System.out.print("Enter employee ID: ");
			empId = sc.nextLine().trim().toUpperCase();
		} else {
			role = Roles.USER;
			empId = Menu.currentUser.getEmpId();
		}
		Employee employee = new Employee();
		employee.setId(empId);
		if (!role.equals(Roles.USER)) {
			System.out.print("Enter first name: ");
			String fname = sc.nextLine().trim().toUpperCase();
			System.out.print("Enter last name: ");
			String lname = sc.nextLine().trim().toUpperCase();
			employee.setName(fname + " " + lname);
			System.out.print("Enter department: ");
			String dept = sc.nextLine().trim().toUpperCase();
			employee.setDept(dept);
		}
		System.out.print("Enter DOB - Day: ");
		String day = sc.nextLine().trim().toUpperCase();
		System.out.print("Enter DOB - Month: ");
		String month = sc.nextLine().trim().toUpperCase();
		System.out.print("Enter DOB - Year: ");
		String year = sc.nextLine().trim().toUpperCase();
		employee.setDob(year + "-" + month + "-" + day);
		System.out.print("Enter address: ");
		employee.setAddress(sc.nextLine().trim().toUpperCase());
		System.out.print("Enter email: ");
		employee.setEmail(sc.nextLine().trim().toUpperCase());
		try {
	        boolean updated = employeeService.update(employee, role, dao);
	        if (updated) {
	            System.out.println("Employee updated successfully!");
	            if (!role.equals(Roles.USER)) {
	                viewAllEmployees(dao);
	            } else {
	                viewEmployeeById(dao);
	            }
	        } else {
	        	   System.out.println("Failed to update employee details");
	        }
		} catch (ValidationException | ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	public void printEmployee(Employee emp) {
	    System.out.println("-----Employee Details----------");
	    System.out.println("ID: " + emp.getId());
	    System.out.println("Name: " + emp.getName());
	    System.out.println("Department: " + emp.getDept());
	    System.out.println("DOB: " + emp.getDob());
	    System.out.println("Address: " + emp.getAddress());
	    System.out.println("Email: " + emp.getEmail());
	    System.out.println("----------------------------");
	}
	public void fetchInActiveEmployees(EmployeeDao dao) {
		
	}

}
