package com.employee.daoFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import com.employee.util.EmployeeUtil;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerSideValidations {
    public static String role;
    public static String id;
    public  boolean validateLogin(String inputId, String password) {
    	 EmployeeUtil util = new EmployeeUtil();
    	 Gson gson = new Gson();
    	  EmployeeDaoImpl daoImp = new EmployeeDaoImpl();
        if (!EmployeeDaoImpl.file.exists() || EmployeeDaoImpl.file.length() == 0) {
            System.out.println("No employee records found");
            return false;
        }
        try (FileReader reader = new FileReader(EmployeeDaoImpl.file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            String hashPassword = util.hash(password);
        	for (JsonElement el : array) {
				JsonObject obj = el.getAsJsonObject();
				String jsonId = obj.get("id").getAsString().toUpperCase();
				String jsonPassword = obj.get("password").getAsString();
				if (jsonId.equals(inputId.trim().toUpperCase())) { // ID is case Insensitive

					if (jsonPassword.equals(hashPassword)) {
						ServerSideValidations.id = jsonId;
						JsonArray roleArray = obj.getAsJsonArray("role");
						if (roleArray != null && roleArray.size() > 0) {
							List<String> roles = gson.fromJson(roleArray, List.class);
							Collections.sort(roles);
							ServerSideValidations.role = roles.get(0);
						} else {
							ServerSideValidations.role = "USER";
						}
						return true;
					}
					return false;
				}
            }
        } catch (Exception e) {
            System.out.println("Login validation error: " + e.getMessage());
        }
        return false;
    }
    public  boolean checkEmpExists(String checkId) {
        if (!EmployeeDaoImpl.file.exists() || EmployeeDaoImpl.file.length() == 0) {
            return false;
        }
        try (FileReader reader = new FileReader(EmployeeDaoImpl.file)) {
            JsonArray employees = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement el : employees) {
                JsonObject emp = el.getAsJsonObject();
                if (emp.get("id").getAsString().equals(checkId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking employee: " + e.getMessage());
        }
        return false;
    }
    public  String generateAutoId() {;
	  	final String prefix = "TEK-";
		int id = 1;
		if (!EmployeeDaoImpl.file.exists() || EmployeeDaoImpl.file.length() == 0) {
			return prefix + id;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(EmployeeDaoImpl.file))) {
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
