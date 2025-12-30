package com.employee.controller;

import java.util.Scanner;
import com.employee.dao.HashPassword;
import com.employee.dto.LoginValid;
import com.employee.controller.Menu;
import com.employee.exceptions.LoginFailedException;
import com.employee.exceptions.InvalidIdException;

public class Login {
	public static void login() {

		Scanner sc = new Scanner(System.in);
		System.out.println(" Employee Management System: ");

		// Validating Admin Credentials

		boolean validUser = false;
		boolean count = false;
		while (!count) {

			System.out.print("Enter ID: ");
			String id = sc.nextLine().trim();

			System.out.print("Enter Password: ");
			String password = sc.nextLine().trim();

			try {

				validUser = LoginValid.login(id, password);

				if (validUser) {
					count = true;
					Menu.displayMenu();
				} else {
					System.out.println("\nWRONG DETAILS. Please relogin.\n");

				}
			} catch (LoginFailedException e) {
				System.out.println(e.getMessage());
			} catch (InvalidIdException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("An unexpected error occurred: " + e.getMessage());
			}
		}
	}
}
