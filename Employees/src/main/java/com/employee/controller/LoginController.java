package com.employee.controller;

import java.util.Scanner;
import com.employee.exceptions.InvalidIdException;
import com.employee.exceptions.LoginFailedException;
import com.employee.services.CheckLogin;

public class LoginController {

	public boolean loginCheck(String filepath) {

		Scanner sc = new Scanner(System.in);
		CheckLogin login = new CheckLogin(filepath);

		System.out.println("Employee Management System");

		while (true) {
			try {
				System.out.println("\nLOGIN");

				System.out.print("Enter Employee ID to LOGIN: ");
				String id = sc.nextLine();

				if (id == null || id.trim().isEmpty()) {
					throw new InvalidIdException("Invalid Employee ID");
				}

				System.out.print("Enter Password to LOGIN: ");
				String password = sc.nextLine();

				if (!login.validateLogin(id, password)) {
					throw new LoginFailedException("Invalid credentials");
				}

				System.out.println("Login successful!");
				return true;

			} catch (InvalidIdException e) {
				System.out.println(e.getMessage());

			} catch (LoginFailedException e) {
				System.out.println(e.getMessage());

			} catch (Exception e) {
				System.out.println("Unexpected error: " + e.getMessage());
			}

			System.out.println("Re-enter your login credentials.");
		}
	}
}
