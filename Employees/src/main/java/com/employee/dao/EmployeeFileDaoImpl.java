package com.employee.dao;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.InvalidIdException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class EmployeeFileDaoImpl implements EmployeeDao {
	public static final File file = new File("src/main/resources/users.json");
	public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	EmployeeUtil util = new EmployeeUtil();
	ServerSideValidations se = new ServerSideValidations();
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
	public void addEmployee(String name, String dept, String dob, String address, String email, List<Roles>rolesArray,
			String hashPassword) {
		JsonArray employees = getDataFromFile();
		JsonObject emp = new JsonObject();
		emp.addProperty("id", se.generateAutoId());
		emp.addProperty("name", name);
		emp.addProperty("dept", dept);
		emp.addProperty("dob", dob);
		emp.addProperty("address", address);
		emp.addProperty("email", email);
		emp.addProperty("password", hashPassword);
		JsonElement rolesJson = gson.toJsonTree(rolesArray);
		emp.add("role", rolesJson);
		employees.add(emp);
		saveToFile(employees);
		System.out.println("Employee added successfully");
	}

	public void updateEmployee(String id, String name, String dept, String dob, String address, String email,Roles role) {
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			JsonObject emp = el.getAsJsonObject();
			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
				emp.addProperty("dob", dob);
				emp.addProperty("address", address);
				emp.addProperty("email", email);
				if (!role.equals(Roles.USER)) {
					emp.addProperty("name", name);
					emp.addProperty("dept", dept);
				}
				break;
			}
		}
		saveToFile(employees);
	}
	public void deleteEmployee(String id) {
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
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
	public void viewAllEmployee() {
		JsonArray employees = getDataFromFile();
		for (JsonElement el : employees) {
			printEmployee(el.getAsJsonObject());
		}
	}
	public void viewEmployeeById(String id) {
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
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
//	public void changePassword(String id, String oldHash, String newHash) {
//		 if (!checkEmpExists(id)) {
//		        throw new InvalidIdException("Employee ID does not exist");
//		    }
//		JsonArray employees = getDataFromFile();
//		for (JsonElement el : employees) {
//			JsonObject emp = el.getAsJsonObject();
//			if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
//				String storedHash = emp.get("password").getAsString();
//				if (!storedHash.equals(oldHash)) {
//					throw new InvalidIdException("Old password is incorrect");
//				}
//				emp.addProperty("password", newHash);
//				saveToFile(employees);
//				return;
//			}
//		}
//		throw new InvalidIdException("Employee ID not found");
//	}
	public void changePassword(String id, String oldPass, String newPass) {
	    if (!checkEmpExists(id)) {
	        throw new InvalidIdException("Employee ID does not exist");
	    }
	    JsonArray employees = getDataFromFile();
	    EmployeeUtil util = new EmployeeUtil(); 
	    boolean updated = false;
	    for (JsonElement el : employees) {
	        JsonObject emp = el.getAsJsonObject();
	        if (emp.get("id").getAsString().equalsIgnoreCase(id)) {
	            String storedHash = emp.get("password").getAsString();
	            if (!util.verify(oldPass, storedHash)) {
	                throw new InvalidIdException("Old password is incorrect");
	            }
	            String newHash = util.hash(newPass);
	            emp.addProperty("password", newHash);
	            saveToFile(employees);
	            updated = true;
	            break;
	        }
	    }
	    if (!updated) {
	        throw new InvalidIdException("Employee ID not found");
	    }
	}

	public void resetPassword(String id, String password) {
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
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
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
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
		 if (!checkEmpExists(id)) {
		        throw new InvalidIdException("Employee ID does not exist");
		    }
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
			throw new EmployeeDoesNotExists("Employee does not exist");
		}
		saveToFile(employees);
	}
//	public LoginResult validateUser(String id, String password) {
//		JsonArray employeesList = getDataFromFile();
//		for (JsonElement element : employeesList) {
//			JsonObject employee = element.getAsJsonObject();
//			if (employee.get("id").getAsString().equalsIgnoreCase(id)
//					&& employee.get("password").getAsString().equals(util.hash(password))) {
//				JsonArray rolesArray = employee.getAsJsonArray("role");
//				List<Roles> roleList = new ArrayList<>();
//				for (JsonElement roleEl : rolesArray) {
//					roleList.add(Roles.valueOf(roleEl.getAsString()));
//				}
//				String empId = employee.get("id").getAsString();
//				return new LoginResult(true, empId, roleList);
//			}
//		}
//		return new LoginResult(false, null, null);
//	}
	public LoginResult validateUser(String id, String password) {

	    JsonArray employeesList = getDataFromFile();

	    for (JsonElement element : employeesList) {
	        JsonObject employee = element.getAsJsonObject();

	        if (employee.get("id").getAsString().equalsIgnoreCase(id)) {

	            String storedHash = employee.get("password").getAsString();

	           
	            if (util.verify(password, storedHash)) {

	                JsonArray rolesArray = employee.getAsJsonArray("role");
	                List<Roles> roleList = new ArrayList<>();

	                for (JsonElement roleEl : rolesArray) {
	                    roleList.add(Roles.valueOf(roleEl.getAsString()));
	                }

	                return new LoginResult(true, employee.get("id").getAsString(), roleList);
	            }
	        }
	    }
	    return new LoginResult(false, null, null);
	}

	  private boolean checkEmpExists(String checkId) {
	        if (!EmployeeFileDaoImpl.file.exists() || EmployeeFileDaoImpl.file.length() == 0) {
	            return false;
	        }
	        try (FileReader reader = new FileReader(EmployeeFileDaoImpl.file)) {
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

