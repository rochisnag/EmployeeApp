package com.employee.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;

public class Idgenerate {

	public static String getId() {

		String prefix = "TEK-";
		JSONParser parser = new JSONParser();
		int id = 1;

		File file = new File("output.json");
		if (!file.exists() || file.length() == 0) {
			return prefix + id;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			Object obj = parser.parse(reader);
			JSONArray employees = (JSONArray) obj;

			if (employees.size() == 0) {
				return prefix + id;
			}

			int maxId = 0;
			for (Object o : employees) {
				JSONObject jsonObj = (JSONObject) o;
				String employeeId = (String) jsonObj.get("id");

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
		} catch (ParseException e) {
			System.out.println("Parsing Error: " + e.getMessage());
		}

		return prefix + id;
	}
}
