package com.employee.dao;

import com.employee.util.EmployeeUtil;
import com.employee.exceptions.InvalidIdException;
import com.employee.services.CheckLogin;
import com.employee.model.Employee;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class EmployeeDaoImpl implements EmployeeDao {

	private final File file;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final EmployeeUtil util;
	public EmployeeDaoImpl(String filepath) {
		this.file = new File(filepath);
		this.util = new EmployeeUtil(filepath);
	}
	private JsonArray getDataFromFile() {
		if (!file.exists() || file.length() == 0) {
			return new JsonArray();
		}
		try (FileReader reader = new FileReader(file)) {
			return JsonParser.parseReader(reader).getAsJsonArray();
		} catch (Exception e) {
			return new JsonArray();
		}
	}
	private void saveToFile(JsonArray array) {
		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(array, writer);
		} catch (IOException e) {
			System.out.println("Unable to save file");
		}
	}
	public void addEmployee(String name, String dept, String dob, String address, String email,
			JsonArray rolesArray, String hashPassword) {
		JsonArray employees = getDataFromFile();
		JsonObject emp = new JsonObject();
		emp.addProperty("id", util.generateId());
		emp.addProperty("name", name);
		emp.addProperty("dept", dept);
		emp.addProperty("dob", dob);
		emp.addProperty("address", address);
		emp.addProperty("email", email);
		emp.addProperty("password", hashPassword);
		emp.add("role", rolesArray);
		employees.add(emp);
		saveToFile(employees);
		System.out.println("Employee added successfully");
	}
	public void updateEmployee(String id, String name,String dept, String dob, String address,
			String email) {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {			
				emp.addProperty("name", name);
				emp.addProperty("dob", dob);
				emp.addProperty("address", address);
				emp.addProperty("email", email);
				if (!"USER".equals(CheckLogin.role)) {
					emp.addProperty("dept", dept);
				}
				break;
			}
		}
		saveToFile(employees);
	}
	public void deleteEmployee(String id) {
		JsonArray employees = getDataFromFile();
		Iterator<JsonElement> iterator = employees.iterator();
		while (iterator.hasNext()) {
			JsonObject emp = iterator.next().getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				iterator.remove();
				break;
			}
		}
		saveToFile(employees);
	}
	public void viewEmployee() {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			printEmployee(el.getAsJsonObject());
		}
	}
	public void viewEmployee_by_id(String id) {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				printEmployee(emp);
				return;
			}
		}
		System.out.println("Employee not found");
	}
	public void changePassword(String id, String oldHash, String newHash) {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				String storedHash = emp.get("password").getAsString();
				if (!storedHash.equals(oldHash)) {
					throw new InvalidIdException("Old password is incorrect");
				}
				emp.addProperty("password", newHash);
				saveToFile(employees);				
				return;
			}
		}
		throw new InvalidIdException("Employee ID not found");
	}
	public void resetPassword(String id, String password) {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				emp.addProperty("password", password);
				saveToFile(employees);
				System.out.println("Password reset successfully");
				return;
			}
		}
		System.out.println("Employee not found");
	}
	public void grantRole(String id, String role) {
		JsonArray employees = getDataFromFile();
		boolean found = false;
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				emp.getAsJsonArray("role").add(role);
				found = true;
				break;
			}
		}
		if (!found) {
			throw new RuntimeException("Employee ID does not exist");
		}
		saveToFile(employees);
	}
	public void revokeRole(String id, String role) {
		JsonArray employees = getDataFromFile();
		boolean found = false;
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				emp.getAsJsonArray("role").remove(new JsonPrimitive(role));
				found = true;
				break;
			}
		}
		if (!found) {
			throw new RuntimeException("Employee ID does not exist");
		}
		saveToFile(employees);
	}
	public Employee getEmployeeById(String id) {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				Employee employee = new Employee();
				employee.setId(emp.get("id").getAsString());
				employee.setName(emp.get("name").getAsString());
				employee.setDept(emp.get("dept").getAsString());
				employee.setDob(emp.get("dob").getAsString());
				employee.setAddress(emp.get("address").getAsString());
				employee.setEmail(emp.get("email").getAsString());
				return employee;
			}
		}
		return null;
	}
	private void printEmployee(JsonObject emp) {
		System.out.println("----------------------");
		System.out.println("ID       : " + emp.get("id").getAsString());
		System.out.println("Name     : " + emp.get("name").getAsString());
		System.out.println("Address  : " + emp.get("address").getAsString());
		System.out.println("Dept     : " + emp.get("dept").getAsString());
		System.out.println("DOB      : " + emp.get("dob").getAsString());
		System.out.println("Email    : " + emp.get("email").getAsString());
		System.out.println("Roles    : " + emp.get("role"));
	}

}
