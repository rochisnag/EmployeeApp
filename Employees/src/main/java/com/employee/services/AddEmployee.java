package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import java.util.List;
import java.util.ArrayList;
public class AddEmployee {
	Employee employee = new Employee();
	private final Scanner sc = new Scanner(System.in);
	EmployeeUtil util = new EmployeeUtil();
    GetEmployee getEmployee = new GetEmployee();
	public void insert(EmployeeDao emp) {
		try {
		System.out.println("Enter emp first name:");
		String fname = sc.nextLine();
		System.out.println("Enter emp last name:");
		String lname = sc.nextLine();
		String name = fname +" "+lname;
		if(!util.validateName(name))
			return;
		System.out.println("Enter emp dept:");
		String dept = sc.nextLine();
		if(!util.validateDept(dept))
			return;
		System.out.println("Enter emp date in DOB:");
		String day = sc.nextLine();
		System.out.println("Enter emp month in  DOB:");
		String month = sc.nextLine();
		System.out.println("Enter emp year in DOB:");
		String year = sc.nextLine();
		String DOB = year + "-" + month + "-" +day;
		if(!util.validateDob(DOB))
			return;
		System.out.println("Enter emp address:");
		String address = sc.nextLine();
		if(!util.validateAddress(address))
			return;
		System.out.println("Enter emp email:");
		String email = sc.nextLine();
		if(!util.validateEmail(email))
			return;	
		System.out.println("Enter emp role with , seperated :");
		System.out.println("Available Roles:");
		for (Roles role : Roles.values()) {
			System.out.println(role);
		}
		String roleInput = sc.nextLine();
       List<Roles> rolesArray = new ArrayList<>();
       for (String role : roleInput.split(",")) { 
    	   Roles validatedRole = util.validateRole(role.trim().toUpperCase());
    	    rolesArray.add(validatedRole);
		}  
		String password = util.generateRandomPassword();
		if(!util.validatePassword(password))
			return;
		String hashPassword = util.hash(password);
		emp.addEmployee(name, dept, DOB, address, email, rolesArray, hashPassword);
		getEmployee.getAll(emp);
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid inputs: " +e.getMessage());
			 e.printStackTrace();
		}
	}
}