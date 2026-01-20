package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.dao.ServerSideValidations;
import com.employee.exceptions.EmployeeDoesNotExists;

public class DeleteEmployee {
	
	ServerSideValidations se = new ServerSideValidations();
	private final Scanner sc = new Scanner(System.in);
	public void delete(EmployeeDao dao) {
		GetEmployee getEmployee = new GetEmployee();
		try {
			System.out.println("Enter empId to delete: ");
			String inputId = sc.nextLine();
			dao.deleteEmployee(inputId.toUpperCase());
			System.out.println("Employee deleted successfully");
			getEmployee.getAll(dao);
		} catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
 