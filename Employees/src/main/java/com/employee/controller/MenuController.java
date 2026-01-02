package com.employee.controller;

import java.util.Scanner;
import com.employee.util.AdminChoices;
import com.employee.util.ManagerChoices;
import com.employee.util.UserChoices;
import com.employee.services.AddEmployee;
import com.employee.services.ChangeRole;
import com.employee.services.CheckLogin;
import com.employee.services.DeleteEmployee;
import com.employee.services.GetEmployee;
import com.employee.services.PasswordOperations;
import com.employee.services.UpdateEmployee;

public class MenuController {

	public static void displayMenu(String filepath) {

		LoginController login = new LoginController();

		if (!login.loginCheck(filepath))
			return;

		GetEmployee readEmployees = new GetEmployee(filepath);
		DeleteEmployee deleteEmployees = new DeleteEmployee(filepath);
		UpdateEmployee updateEmployees = new UpdateEmployee(filepath);
		PasswordOperations passwordOperations = new PasswordOperations(filepath);
		AddEmployee addEmployee = new AddEmployee(filepath);
		ChangeRole changeRole = new ChangeRole(filepath);

		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		String role = CheckLogin.role;

		System.out.println("\nEMPLOYEE MANAGEMENT SYSTEM\n");

		while (!exit) {
			if ("ADMIN".equalsIgnoreCase(role)) {
				System.out.println("ADMIN OPERATIONS\n");
				for (AdminChoices c : AdminChoices.values())
					System.out.println(c);

				try {
					System.out.println("\nType your Choice: ");
					AdminChoices choice = AdminChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case ADD:
						addEmployee.insert(filepath);
						break;
					case VIEW:
						readEmployees.get_all();
						break;
					case DELETE:
						deleteEmployees.delete();
						break;
					case UPDATE:
						updateEmployees.update();
						break;
					case VIEW_BY_ID:
						readEmployees.get_by_id();
						break;
					case RESET_PASSWORD:
						passwordOperations.resetPassword();
						break;
					case GRANT_ROLE:
						changeRole.grantRole();
						break;
					case REVOKE_ROLE:
						changeRole.revokeRole();
						break;
					case EXIT:
						exit = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid choice");
				}

			} else if ("MANAGER".equalsIgnoreCase(role)) {
				System.out.println("MANAGER OPERATIONS\n");
				for (ManagerChoices c : ManagerChoices.values())
					System.out.println(c);

				try {
					System.out.println("\nType your Choice: ");
					ManagerChoices choice = ManagerChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case VIEW:
						readEmployees.get_all();
						break;
					case UPDATE:
						updateEmployees.update();
						break;
					case VIEW_BY_ID:
						readEmployees.get_by_id();
						break;
					case EXIT:
						exit = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid choice");
				}

			} else {
				System.out.println("USER OPERATIONS\n");
				for (UserChoices c : UserChoices.values())
					System.out.println(c);

				try {
					System.out.println("\nType your Choice: ");
					UserChoices choice = UserChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case VIEW:
						readEmployees.get_by_id();
						break;
					case CHANGE_PASSWORD:
						passwordOperations.changePassword();
						break;
					case UPDATE:
						updateEmployees.update();
						break;
					case EXIT:
						exit = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid choice");
				}
			}
		}
	}
}
