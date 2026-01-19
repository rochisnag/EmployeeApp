package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeFileDaoImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.InvalidIdException;
import com.employee.controller.MenuController;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordOperations {
	EmployeeDao dao = new EmployeeFileDaoImpl();
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	ServerSideValidations se = new ServerSideValidations();
	private final Scanner sc = new Scanner(System.in);
	private static String getHashedDefaultPassword() {
	    String defaultPassword = System.getenv("DefaultPassword");
	    if (defaultPassword == null || defaultPassword.isEmpty()) {
	        throw new IllegalStateException(
	            "DEFAULT_PASSWORD environment variable is not set"
	        );
	    }
	    EmployeeUtil util = new EmployeeUtil();
	    return util.hash(defaultPassword);   
	}
	public void changePassword(EmployeeDao dao) {
		String id =  MenuController.currentUser.getEmpId();
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
			
			dao.changePassword(id.toUpperCase(), oldPassword, newPassword);
		
		} catch (InvalidIdException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
	public void resetPassword(EmployeeDao dao) {
		try {
		System.out.print("Enter employee ID to reset password:");
		String id = sc.nextLine();	
			String hashPassword = getHashedDefaultPassword();
			dao.resetPassword(id.toUpperCase(), hashPassword);
            System.out.println("Password reset successfully for employee: " + id.toUpperCase() );
		}catch (InvalidIdException e) {
			System.out.println(e.getMessage());
		}catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
}
