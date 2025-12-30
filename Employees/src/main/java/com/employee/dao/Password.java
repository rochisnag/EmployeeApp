package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.employee.dao.Json;
import com.google.gson.JsonElement;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.InvalidIdException;

public class Password {

	private static final String DEFAULT_PASSWORD = "Admin@123";

	public static void changePassword() {

		Scanner sc = new Scanner(System.in);
		JsonArray users = Json.loadUsers();

		System.out.print("Enter User ID: ");
		String userId = sc.nextLine();

		System.out.print("Enter Old Password: ");
		String oldPassword = sc.nextLine();
		String oldHash = HashPassword.hashPassword(oldPassword);

		boolean found = false;

		for (JsonElement obj : users) {

			JsonObject user = obj.getAsJsonObject();

			if (user.get("id") != null && user.get("id").getAsString().equals(userId)) {

				found = true;
				String storedHash = user.get("password").getAsString();

				if (!storedHash.equals(oldHash)) {
					System.out.println("Old password does not match!");
					return;
				}

				System.out.print("Enter New Password: ");
				String newPassword = sc.nextLine();

				String newHash = HashPassword.hashPassword(newPassword);
				user.addProperty("password", newHash);

				try {
					Json.saveUsers(users);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Password changed successfully!");
				return;
			}
		}
		if (!found) {
			try {
				throw new InvalidIdException("User ID not found");
			} catch (InvalidIdException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// ================= RESET PASSWORD (ADMIN) =================
	public static void resetPassword() throws Exception {

		Scanner sc = new Scanner(System.in);
		JsonArray users = Json.loadUsers();

		System.out.print("Enter User ID to reset password: ");
		String userId = sc.nextLine();

		boolean found = false;
		String defaultHash = HashPassword.hashPassword(DEFAULT_PASSWORD);

		for (JsonElement obj : users) {
			JsonObject user = obj.getAsJsonObject();

			if (user.get("id").getAsString().equals(userId)) {

				found = true;
				user.addProperty("password", defaultHash);

				Json.saveUsers(users);
				System.out.println("Password reset to default successfully!");
				System.out.println("Default Password: " + DEFAULT_PASSWORD);
				return;
			}
		}

		if (!found) {
			try {
				throw new InvalidIdException("User ID not found");
			} catch (InvalidIdException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
