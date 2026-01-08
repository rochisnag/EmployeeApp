package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.daoFile.ServerSideValidations;
import com.employee.exceptions.EmployeeDoesNotExists;

public class DeleteEmployee {
	EmployeeDao dao = new EmployeeDaoImpl();
	ServerSideValidations se = new ServerSideValidations();
	private final Scanner sc = new Scanner(System.in);
	public void delete() {
		GetEmployee getEmployee = new GetEmployee();
		try {
			System.out.println("Enter empId to delete: ");
			String inputId = sc.nextLine();
			boolean present = se.checkEmpExists(inputId.toUpperCase());
			if (!present) {
				throw new EmployeeDoesNotExists("Employee doesnot exist");
			}
			dao.deleteEmployee(inputId.toUpperCase());
			System.out.println("Employee deleted successfully");
			getEmployee.get_all();
		} catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
