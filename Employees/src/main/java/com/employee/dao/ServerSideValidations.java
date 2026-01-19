package com.employee.dao;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerSideValidations {
    public  String generateAutoId(){;
	  	final String prefix = "TEK-";
		int id = 1;
		if (!EmployeeFileDaoImpl.file.exists() || EmployeeFileDaoImpl.file.length() == 0) {
			return prefix + id;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(EmployeeFileDaoImpl.file))) {
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
			System.out.println("File Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Parsing Error: " + e.getMessage());
		}
		return prefix + id;
	}
}
