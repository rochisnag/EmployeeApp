package com.employee.dao;

import java.io.IOException;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.employee.dao.Json;
import com.employee.dao.View;
import com.employee.dto.LoginValid;
import com.employee.exceptions.EmployeeDoesNotExists;

public class Update {

	public static void execute() {
		try {
			Scanner sc = new Scanner(System.in);
			JsonArray users = Json.loadUsers();

			String id;

			if (LoginValid.role.equals("USER")) {
				id = LoginValid.id;
			} else {
				System.out.print("Enter Employee ID: ");
				id = sc.nextLine().trim();
			}

			JsonObject user = findUserById(users, id);
			if (user == null) {
				throw new EmployeeDoesNotExists("Employee ID '" + id + "' does not exist.");
			}

			System.out.println("\n--- UPDATE EMPLOYEE DETAILS ---");

			if (!LoginValid.role.equals("USER")) {
				System.out.print("Enter First Name: ");
				String fname = sc.nextLine().trim();

				System.out.print("Enter Last Name: ");
				String lname = sc.nextLine().trim();

				user.addProperty("fname", fname);
				user.addProperty("lname", lname);

				System.out.print("Enter Department: ");
				String dept = sc.nextLine().trim();
				user.addProperty("dept", dept);
			}

			System.out.print("Enter Day of Birth: ");
			String day = sc.nextLine().trim();
			System.out.print("Enter Month of Birth: ");
			String month = sc.nextLine().trim();
			System.out.print("Enter Year of Birth: ");
			String year = sc.nextLine().trim();
			String dob = day + "-" + month + "-" + year;
			user.addProperty("dob", dob);

			System.out.print("Enter Address: ");
			String address = sc.nextLine().trim();
			user.addProperty("address", address);

			System.out.print("Enter Email: ");
			String email = sc.nextLine().trim();
			user.addProperty("email", email);

			Json.saveUsers(users);

			System.out.println("\nEmployee updated successfully!");

			if (!LoginValid.role.equals("USER")) {
				View.showAll();
			} else {
				View.showOne();
			}

		} catch (Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
		}
	}

	private static JsonObject findUserById(JsonArray users, String id) {
		for (int i = 0; i < users.size(); i++) {
			JsonObject user = users.get(i).getAsJsonObject();
			if (user.get("id").getAsString().equals(id)) {
				return user;
			}
		}
		return null;
	}
}
