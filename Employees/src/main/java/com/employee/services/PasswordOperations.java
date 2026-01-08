package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.daoFile.ServerSideValidations;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.InvalidIdException;

public class PasswordOperations {
	EmployeeDao dao = new EmployeeDaoImpl();
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	ServerSideValidations se = new ServerSideValidations();
	private final Scanner sc = new Scanner(System.in);
	public static final String defaultpass = "pass123";
	public void changePassword() {
		String id = ServerSideValidations.id;
		try {
			System.out.println("Enter old password:");
			String oldPassword = sc.nextLine();
			
			System.out.println("Enter new password:");
			String newPassword = sc.nextLine();
			System.out.println("Re-enter new password:");
			String confirmPassword = sc.nextLine();
			if (!newPassword.equals(confirmPassword)) {
				System.out.println("Passwords didn't match");
				return;
			}
			String oldHash = util.hash(oldPassword);
			String newHash = util.hash(newPassword);
			dao.changePassword(id.toUpperCase(), oldHash, newHash);
			System.out.println("Password changed successfully");
		} catch (InvalidIdException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
	public void resetPassword() {
		try {
		System.out.print("Enter employee ID to reset password:");
		String id = sc.nextLine();	
		boolean exists = se.checkEmpExists(id.toUpperCase());
		if (exists) {
			String hashPassword = util.hash(defaultpass);
			dao.resetPassword(id.toUpperCase(), hashPassword);
			System.out.println("Password reset successfully to default:" + defaultpass);
		}else {
			throw new EmployeeDoesNotExists("Employee doesnot exist");
		}
		}catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
}
