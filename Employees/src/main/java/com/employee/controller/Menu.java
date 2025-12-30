package com.employee.controller;

import java.util.Scanner;
import com.employee.dao.Insert;
import com.employee.dao.Delete;
import com.employee.dao.View;
import com.employee.dao.Password;
import com.employee.dao.Update;
import com.employee.enums.AdminChoices;
import com.employee.enums.ManagerChoices;
import com.employee.enums.UserChoices;
import com.employee.dao.Privilege;
import com.employee.dto.LoginValid;

public class Menu {

	public static final Scanner sc = new Scanner(System.in);

	public static void displayMenu() throws Exception {

		boolean exit = false;
		String role = LoginValid.role;

		while (!exit) {

			if (role.equalsIgnoreCase("ADMIN")) {
				System.out.println("\nADMIN OPERATIONS");
				for (AdminChoices c : AdminChoices.values()) {
					System.out.println(c);
				}

				try {
					System.out.print("Enter choice: ");
					AdminChoices choice = AdminChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case ADD:
						Insert.execute();
						break;
					case VIEW:
						View.showAll();
						break;
					case DELETE:
						Delete.execute();
						break;
					case UPDATE:
						Update.execute();
						break;
					case VIEW_BY_ID:
						View.showOne();
						break;
					case RESET_PASSWORD:
						Password.resetPassword();
						break;
					case GRANT:
						Privilege.grant();
						break;
					case REVOKE:
						Privilege.revoke();
						break;
					case EXIT:
						exit = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid choice");
				}

			} else if (role.equalsIgnoreCase("MANAGER")) {
				System.out.println("\nMANAGER OPERATIONS");
				for (ManagerChoices c : ManagerChoices.values()) {
					System.out.println(c);
				}

				try {
					System.out.print("Enter choice: ");
					ManagerChoices choice = ManagerChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case VIEW:
						View.showAll();
						break;
					case UPDATE:
						Update.execute();
						break;
					case VIEW_BY_ID:
						View.showOne();
						break;
					case EXIT:
						exit = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid choice");
				}

			} else {
				System.out.println("\nUSER OPERATIONS");
				for (UserChoices c : UserChoices.values()) {
					System.out.println(c);
				}

				try {
					System.out.print("Enter choice: ");
					UserChoices choice = UserChoices.valueOf(sc.nextLine().toUpperCase());

					switch (choice) {
					case VIEW:
						View.showOne();
						break;
					case CHANGE_PASSWORD:
						Password.changePassword();
						break;
					case UPDATE:
						Update.execute();
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
