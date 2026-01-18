package com.employee.services;
import java.util.Scanner;
import com.employee.model.LoginResult;
import com.employee.dao.EmployeeDao;
public class EmployeeLogin {
	Scanner sc = new Scanner(System.in);
	public LoginResult validate(EmployeeDao dao){
		while (true) {
			System.out.println("Enter id:");
			String id = sc.next().toUpperCase();
			System.out.println("Enter password");
			String password = sc.next();
			LoginResult login=dao.validateUser(id, password);
			if(login.getSuccess()) {
				return login;
			}
			else {
				System.out.println("Invalid credentials");
				continue;
			}
		}
}
}
