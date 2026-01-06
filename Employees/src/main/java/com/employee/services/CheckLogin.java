package com.employee.services;

import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

import com.employee.util.EmployeeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CheckLogin {
	public static String role;
	public static String id;
	private final File file;
	private final Gson gson = new Gson();
	private final EmployeeUtil util;
	
	public CheckLogin(String filepath) {
		this.file = new File(filepath);
		this.util = new EmployeeUtil(filepath);
	}
	public boolean validateLogin(String inputId, String password) {
		if (!file.exists() || file.length() <= 2) {
			System.out.println("No login employees");
			return false;
		}
		try (FileReader reader = new FileReader(file)) {
			JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
			String hashPassword = util.hash(password);
			for (JsonElement el : array) {
				JsonObject obj = el.getAsJsonObject();
				String jsonId = obj.get("id").getAsString().toUpperCase();
				String jsonPassword = obj.get("password").getAsString();
				if (jsonId.equals(inputId.trim().toUpperCase())) { // ID is case Insensitive

					if (jsonPassword.equals(hashPassword)) {
						CheckLogin.id = jsonId;
						JsonArray roleArray = obj.getAsJsonArray("role");
						if (roleArray != null && roleArray.size() > 0) {
							List<String> roles = gson.fromJson(roleArray, List.class);
							Collections.sort(roles);
							CheckLogin.role = roles.get(0);
						} else {
							CheckLogin.role = "USER";
						}
						return true;
					}
					return false;
				}
			}

		} catch (Exception e) {
			System.out.println("Error reading login file: " + e.getMessage());
		}
		return false;
	}
}
