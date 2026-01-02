package com.employee.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.employee.services.GetEmployee;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EmployeeUtil {

	private final File file;

	public EmployeeUtil(String filepath) {
		this.file = new File(filepath);
	}

	public boolean checkEmployee(String checkId) {

		if (!file.exists() || file.length() == 0) {
			return false;
		}

		try (FileReader reader = new FileReader(file)) {
			JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

			for (JsonElement el : array) {
				JsonObject jsonObject = el.getAsJsonObject();
				String id = jsonObject.get("id").getAsString();
				if (id.equals(checkId)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file");
		} catch (Exception e) {
			System.out.println("Parser Error");
		}
		return false;
	}

	// Generate a unique employee ID
	public String generateId() {
		final String prefix = "TEK-";
		int id = 1;

		if (!file.exists() || file.length() == 0) {
			return prefix + id;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			JsonArray employees = JsonParser.parseReader(reader).getAsJsonArray();
			if (employees.size() == 0) {
				return prefix + id;
			}

			int maxId = 0;
			for (JsonElement el : employees) {
				JsonObject jsonObj = el.getAsJsonObject();
				String employeeId = jsonObj.get("id").getAsString();
				if (employeeId != null && employeeId.startsWith(prefix)) {
					String[] parts = employeeId.split("-");
					try {
						int currentId = Integer.parseInt(parts[1]);
						maxId = Math.max(maxId, currentId);
					} catch (NumberFormatException e) {
						System.out.println("Invalid ID format for employee: " + employeeId);
					}
				}
			}
			id = maxId + 1;
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Parsing Error: " + e.getMessage());
		}

		return prefix + id;
	}

	// Hash password using SHA-256
	public String hash(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error hashing password");
			return "";
		}
	}
}
