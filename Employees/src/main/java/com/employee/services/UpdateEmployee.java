package com.employee.services;

import java.util.Scanner;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class UpdateEmployee {

	private final EmployeeDao dao;
	private final EmployeeUtil util;
	private final GetEmployee getEmployee;
	private final Scanner sc = new Scanner(System.in);
	Employee employee = new Employee();

	public UpdateEmployee(String filepath) {
		this.dao = new EmployeeDaoImpl(filepath);
		this.util = new EmployeeUtil(filepath);
		this.getEmployee = new GetEmployee(filepath);
	}

	public void update() {
		String id;
		if (CheckLogin.role.equals("USER")) {
			id = CheckLogin.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.nextLine().trim().toUpperCase();
		}
		boolean exists = util.checkEmployee(id);
		if (!exists) {
			System.out.println("Invalid employee id");
			return;
		}
		String name;
		String dept;
		Employee existing = dao.getEmployeeById(id);
		if (CheckLogin.role.equals("USER")) {
			name = existing.getName();
			dept = existing.getDept();
		}
		else {
			System.out.println("Enter emp first name:");
			 String fname = sc.nextLine();
			System.out.println("Enter emp last name:");
			 String lname = sc.nextLine();	 
			name = fname+" "+lname;
			System.out.println("Enter emp dept:");
			dept = sc.nextLine();
		}
		// Update DOB
		System.out.println("Enter DOB - Day:");
		String day = sc.nextLine();

		System.out.println("Enter DOB - Month:");
		String month = sc.nextLine();

		System.out.println("Enter DOB - Year:");
		String year = sc.nextLine();

		String DOB = day + "-" + month + "-" + year;
		// Update address
		System.out.println("Enter employee address:");
		String address = sc.nextLine();
		// Update email
		System.out.println("Enter employee email:");
		String email = sc.nextLine();
		dao.updateEmployee(id, name, dept, DOB, address, email);
		if (!"USER".equals(CheckLogin.role)) {
			getEmployee.get_all();
		} else {
			getEmployee.get_by_id();
		}
		System.out.println("Employee updated successfully");
	}

}
