package com.employee.services;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
public class AddEmployee {
	Employee employee = new Employee();
	private final Scanner sc = new Scanner(System.in);
    EmployeeDao dao = new EmployeeDaoImpl();
	EmployeeUtil util = new EmployeeUtil();
    GetEmployee getEmployee = new GetEmployee();
	public void insert() {
		try {
		System.out.println("Enter emp first name:");
		String fname = sc.nextLine();

		System.out.println("Enter emp last name:");
		String lname = sc.nextLine();

		String name = fname +" "+lname;
		employee.setName(name);

		System.out.println("Enter emp dept:");
		String dept = sc.nextLine();
		employee.setDept(dept);

		System.out.println("Enter emp date in DOB:");
		String day = sc.nextLine();

		System.out.println("Enter emp month in  DOB:");
		String month = sc.nextLine();

		System.out.println("Enter emp year in DOB:");
		String year = sc.nextLine();

		String DOB = day + "-" + month + "-" + year;
		employee.setDob(DOB);

		System.out.println("Enter emp address:");
		String address = sc.nextLine();
		employee.setAddress(address);

		System.out.println("Enter emp email:");
		String email = sc.nextLine();
		employee.setEmail(email);

		System.out.println("Enter emp role with , seperated :");
		String roleInput = sc.nextLine();

		JsonArray rolesArray = new JsonArray();
		for (String r : roleInput.split(",")) {
			rolesArray.add(r.trim().toUpperCase());
		}
		String password = PasswordOperations.defaultpass;
		employee.setPassword(password);
		String hashPassword = util.hash(password);
		dao.addEmployee(name, dept, DOB, address, email, rolesArray, hashPassword);
		getEmployee.get_all();
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid inputs: " +e.getMessage());
		}
	}
}
