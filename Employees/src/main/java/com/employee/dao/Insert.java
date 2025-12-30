package com.employee.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.simple.parser.ParseException;
import com.employee.dao.Json;
import com.employee.enums.Roles;
import com.employee.dao.HashPassword;
import com.employee.dto.EmpModel;
import com.employee.controller.Menu;

public class Insert {

	public static void execute() {

		Scanner sc = Menu.sc;
		EmpModel em = new EmpModel();
		JsonArray usersArray = Json.loadUsers();

		System.out.print("Enter First Name: ");
		String fname = sc.nextLine();

		System.out.print("Enter Last Name: ");
		String lname = sc.nextLine();

		System.out.print("Enter Department: ");
		String dept = sc.nextLine();
		em.setDept(dept);

		System.out.print("Enter Day: ");
		String day = sc.nextLine();

		System.out.print("Enter Month: ");
		String month = sc.nextLine();

		System.out.print("Enter Year: ");
		String year = sc.nextLine();

		String dob = day + "-" + month + "-" + year;
		em.setDob(dob);

		System.out.print("Enter Address: ");
		String address = sc.nextLine();
		em.setAddress(address);

		System.out.print("Enter Email: ");
		String email = sc.nextLine();
		em.setEmail(email);

		System.out.print("Enter Password: ");
		String password = sc.nextLine();

		String hashedPassword = HashPassword.hashPassword(password);

		System.out.println("\nAvailable Roles:");
		for (Roles r : Roles.values()) {
			System.out.println("- " + r);
		}

		JsonArray rolesArray = new JsonArray();

		System.out.print("Enter number of roles: ");
		int roleCount = Integer.parseInt(sc.nextLine());

		for (int i = 0; i < roleCount; i++) {
			System.out.print("Enter role " + (i + 1) + ": ");
			String roleInput = sc.nextLine().toUpperCase();

			try {
				Roles.valueOf(roleInput);
				rolesArray.add(roleInput);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid role: " + roleInput);
				i--;
			}
		}

		String id = Idgenerate.getId();

		JsonObject userJson = new JsonObject();

		userJson.addProperty("id", id);
		userJson.addProperty("fname", fname);
		userJson.addProperty("lname", lname);
		userJson.addProperty("dept", dept);
		userJson.addProperty("dob", dob);
		userJson.addProperty("address", address);
		userJson.addProperty("email", email);
		userJson.addProperty("password", hashedPassword);
		userJson.add("role", rolesArray);

		usersArray.add(userJson);
		Json.saveUsers(usersArray);

		System.out.println("\n Added successfully!");
		System.out.println("Generated ID: " + id);
	}
}
