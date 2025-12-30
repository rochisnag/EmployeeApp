package com.employee.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Json {
	private static final String FILE_NAME = "output.json";

	public static JsonArray loadUsers() {
		try {
			File file = new File(FILE_NAME);

			if (!file.exists())
				return new JsonArray();

			try (Reader reader = new FileReader(file)) {
				return JsonParser.parseReader(reader).getAsJsonArray();
			}

		} catch (Exception e) {
			System.out.println("Failed to load users.");
		}
		return new JsonArray();
	}

	public static void saveUsers(JsonArray users) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try (FileWriter writer = new FileWriter(FILE_NAME)) {
			gson.toJson(users, writer);
		} catch (Exception e) {
			System.out.println("Failed to save users.");

		}
	}
}
