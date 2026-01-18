package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.dao.ServerSideValidations;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import com.employee.controller.MenuController;
public class UpdateEmployee {
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	private final Scanner sc = new Scanner(System.in);
	ServerSideValidations se = new ServerSideValidations();
	Employee employee = new Employee();
	public void update(EmployeeDao dao) {
		String id;
		Roles role = null;
		if (MenuController.currentUser.getRoles().contains(Roles.ADMIN)
				|| MenuController.currentUser.getRoles().contains(Roles.MANAGER)) {
			role = Roles.ADMIN;
		}
		else {
			role = Roles.USER;
		}
		if (role.equals(Roles.USER)) {
			id = MenuController.currentUser.getEmpId();
		} else {
			System.out.print("Enter emp id:");
			id = sc.nextLine().toUpperCase();
		}
		try {
		String name = "";
		String dept = "";
		if (!role.equals(Roles.USER)) {
			System.out.println("Enter emp first name:");
			 String fname = sc.nextLine();
			System.out.println("Enter emp last name:");
			 String lname = sc.nextLine();	 
			name = fname+" "+lname;
			employee.setName(name);
			System.out.println("Enter emp dept:");
			dept = sc.nextLine();
			employee.setDept(dept);
		}
		System.out.println("Enter DOB - Day:");
		String day = sc.nextLine();
		System.out.println("Enter DOB - Month:");
		String month = sc.nextLine();
		System.out.println("Enter DOB - Year:");
		String year = sc.nextLine();
		String DOB = year + "-" + month + "-" + day;
	     employee.setDob(DOB);
		System.out.println("Enter employee address:");
		String address = sc.nextLine();
		 employee.setAddress(address);
		System.out.println("Enter employee email:");
		String email = sc.nextLine();
		employee.setEmail(email);
		dao.updateEmployee(id, name, dept, DOB, address, email,role);	
		if (!role.equals(Roles.USER)) {
			getEmployee.get_all(dao);
		} else {
			getEmployee.get_by_id(dao);
		}
		System.out.println("Employee updated successfully");
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid input: "+e.getMessage());
		}
	}
}
