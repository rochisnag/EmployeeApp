package com.employee.dao;

import java.io.FileReader;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.dto.LoginValid;

public class View {
	public static JSONArray readjson() {
		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		try (FileReader reader = new FileReader("output.json")) {
			Object obj = parser.parse(reader);
			jsonArray = (JSONArray) obj;
		} catch (Exception e) {
			jsonArray = new JSONArray();
		}
		return jsonArray;
	}

	public static void showAll() {
		JSONArray users = readjson();

		if (users.isEmpty()) {
			System.out.println("No employee records found.");
			return;
		}

		System.out.println("\n--- ALL EMPLOYEE DETAILS ---");
		for (Object obj : users) {
			JSONObject user = (JSONObject) obj;
			printEmployee(user);
			System.out.println("-----------------------------");
		}
	}

	public static void showOne() {

		Scanner sc = new Scanner(System.in);
		JSONArray users = readjson();

		String id;

		if (LoginValid.role.equals("USER")) {
			id = LoginValid.id;
		} else {
			System.out.print("Enter Employee ID: ");
			id = sc.nextLine().trim();
		}

		boolean found = false;

		for (Object obj : users) {
			JSONObject user = (JSONObject) obj;
			if (id.equals(user.get("id"))) {
				System.out.println("\n--- EMPLOYEE DETAILS ---");
				printEmployee(user);
				found = true;
				break;
			}
		}

		if (!found) {
			throw new EmployeeDoesNotExists("Employee with ID " + id + " does not exist.");
		}

	}

	private static void printEmployee(JSONObject user) {
		System.out.println("ID       : " + user.get("id"));
		System.out.println("Fname    : " + user.get("fname"));
		System.out.println("Lname    : " + user.get("lname"));
		System.out.println("Dept     : " + user.get("dept"));
		System.out.println("Dob      : " + user.get("dob"));
		System.out.println("Address  : " + user.get("address"));
		System.out.println("Email    : " + user.get("email"));
		System.out.println("Role     : " + user.get("role"));
	}
}
