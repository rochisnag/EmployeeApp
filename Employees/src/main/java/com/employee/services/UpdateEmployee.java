package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.daoFile.ServerSideValidations;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class UpdateEmployee {
    EmployeeDao dao = new EmployeeDaoImpl();
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	private final Scanner sc = new Scanner(System.in);
	ServerSideValidations se = new ServerSideValidations();
	Employee employee = new Employee();
	public void update() {
		String id;
		if (ServerSideValidations.role.equals("USER")) {
			id = ServerSideValidations.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.nextLine().trim().toUpperCase();
		}
		boolean exists = se.checkEmpExists(id);
		if (!exists) {
			System.out.println("Invalid employee id");
			return;
		}
		try {
		String name = "";
		String dept = "";
		if (!ServerSideValidations.role.equals("USER")) {
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
		String DOB = day + "-" + month + "-" + year;
	     employee.setDob(DOB);
		System.out.println("Enter employee address:");
		String address = sc.nextLine();
		 employee.setAddress(address);
		System.out.println("Enter employee email:");
		String email = sc.nextLine();
		employee.setEmail(email);
		dao.updateEmployee(id, name, dept, DOB, address, email);	
		if (!"USER".equals(ServerSideValidations.role)) {
			getEmployee.get_all();
		} else {
			getEmployee.get_by_id();
		}
		System.out.println("Employee updated successfully");
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid input: "+e.getMessage());
		}
	}
}
