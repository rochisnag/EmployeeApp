package com.employee.services;

import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.util.EmployeeUtil;

public class DeleteEmployee {

	private final EmployeeDao dao;
	private final String filepath;
	private final Scanner sc = new Scanner(System.in);

	public DeleteEmployee(String filepath) {
		this.filepath = filepath;
		this.dao = new EmployeeDaoImpl(filepath);
	}

	public void delete() {

		EmployeeUtil util = new EmployeeUtil(filepath);
		GetEmployee getEmployee = new GetEmployee(filepath);

		try {
			System.out.println("Enter empId to delete: ");
			String delId = sc.nextLine();

			boolean present = util.checkEmployee(delId);

			if (!present) {
				throw new EmployeeDoesNotExists("Employee doesnot exist");
			}

			dao.deleteEmployee(delId);
			System.out.println("Employee deleted successfully");
			getEmployee.get_all();

		} catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
