package com.employee.dao;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import com.employee.model.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileDaoImpl implements EmployeeDao {
    public static final File file = new File("src/main/resources/users.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ServerSideValidations se = new ServerSideValidations();
    
      private JsonArray getDataFromFile() {
        try (FileReader reader = new FileReader(file)) {
            if (!file.exists() || file.length() == 0) {
                System.out.println("File is empty or doesn't exist.");
                return new JsonArray();
            }
            JsonArray data = JsonParser.parseReader(reader).getAsJsonArray();
            return data;
        } catch (IOException | JsonParseException e) {
            throw new DataAccessException("Error accessing employees file", e);
        }
    }

    private void saveToFile(JsonArray array) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(array, writer);
        } catch (IOException e) {
            throw new DataAccessException("Unable to save employees file", e);
        }
    }
    
    private void checkActiveEmployee(String id) {
        for (JsonElement e : getDataFromFile()) {
        	 JsonObject employee = e.getAsJsonObject();
            if (employee.getAsJsonObject().get("id").getAsString().equalsIgnoreCase(id) && employee.get("isActive").getAsBoolean()) {
                return;
            }
        }
        throw new EmployeeDoesNotExists("Employee does not exist with ID: " + id);
    }
   
    public void addEmployee(Employee emp) {
        JsonArray employees = getDataFromFile();
        JsonObject o = new JsonObject();
        o.addProperty("id", se.generateAutoId());
        o.addProperty("name", emp.getName());
        o.addProperty("dept", emp.getDept());
        o.addProperty("dob", emp.getDob());
        o.addProperty("address", emp.getAddress());
        o.addProperty("email", emp.getEmail());
        o.addProperty("password", emp.getPassword());
    	o.addProperty("isActive", true);
		o.addProperty("deletedAt", "");
        JsonArray rolesArray = new JsonArray();
        if (emp.getRoles() != null) {
            for (Roles r : emp.getRoles()) {
                rolesArray.add(r.name());
            }
        }
        o.add("roles", rolesArray);  
        employees.add(o);
        saveToFile(employees);
    }
    public boolean updateEmployee(Employee emp, Roles role) {
        checkActiveEmployee(emp.getId());
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
            JsonObject obj = el.getAsJsonObject();
            if (obj.get("id").getAsString().equalsIgnoreCase(emp.getId())) {
                obj.addProperty("dob", emp.getDob());
                obj.addProperty("address", emp.getAddress());
                obj.addProperty("email", emp.getEmail());
                if (!role.equals(Roles.USER)) {
                    obj.addProperty("name", emp.getName());
                    obj.addProperty("dept", emp.getDept());
                }
                break;
            }
        }
        saveToFile(employees);
        return true;
    }
    
    public boolean deleteEmployee(String id) {
        checkActiveEmployee(id);
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
        JsonObject obj = el.getAsJsonObject();
        if (obj.get("id").getAsString().equalsIgnoreCase(id)) {
            obj.addProperty("isActive", false); 
            obj.addProperty("deletedAt", String.valueOf(System.currentTimeMillis())); 
            saveToFile(employees);
            return true; 
        }
    }
    return false;
    }
    public List<Employee> viewAllEmployee() {
        List<Employee> list = new ArrayList<>();
        for (JsonElement el : getDataFromFile()) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("isActive").getAsBoolean()) {  
            list.add(new Employee(
                    o.get("id").getAsString(),
                    o.get("name").getAsString(),
                    o.get("dept").getAsString(),
                    o.get("dob").getAsString(),
                    o.get("address").getAsString(),
                    o.get("email").getAsString()
            ));
        }
        }
        return list;
    }
    public Employee viewEmployeeById(String id) {
        checkActiveEmployee(id);
        for (JsonElement el : getDataFromFile()) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                return new Employee(
                        id,
                        o.get("name").getAsString(),
                        o.get("dept").getAsString(),
                        o.get("dob").getAsString(),
                        o.get("address").getAsString(),
                        o.get("email").getAsString()
                );
            }
        }
        return null;
    }
    public boolean changePassword(String id, String oldPass, String newPass) {
        checkActiveEmployee(id);
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                if (!EmployeeUtil.verify(oldPass, o.get("password").getAsString())) {
                    throw new DataAccessException("Old password incorrect");
                }
                o.addProperty("password", EmployeeUtil.hash(newPass));
                saveToFile(employees);
                return true;
            }
        }
        return false;
    }
    public boolean resetPassword(String id, String password) {
        checkActiveEmployee(id);
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                o.addProperty("password", password);
                saveToFile(employees);
                return true;
            }
        }
        return false;
    }
    public boolean grantRole(String id, Roles role) {
        checkActiveEmployee(id);
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                JsonArray roles = o.getAsJsonArray("roles");
                if (roles == null) {
                    roles = new JsonArray();
                    o.add("roles", roles);
                }
                for (JsonElement r : roles) {
                    if (r.getAsString().equalsIgnoreCase(role.name())) {
                        return false;
                    }
                }
                roles.add(role.name());
                saveToFile(employees);
                return true;
            }
        }
        return false;
    }
    public boolean revokeRole(String id, Roles role) {
        checkActiveEmployee(id);
        JsonArray employees = getDataFromFile();
        for (JsonElement el : employees) {
            JsonObject o = el.getAsJsonObject();
            if (o.get("id").getAsString().equalsIgnoreCase(id)) {
                JsonArray roles = o.getAsJsonArray("roles");
                JsonArray newRoles = new JsonArray();
                boolean removed = false;
                for (JsonElement r : roles) {
                    if (r.getAsString().equalsIgnoreCase(role.name())) {
                        removed = true;
                    } else {
                        newRoles.add(r);
                    }
                }
                if (!removed) {
                    throw new DataAccessException("Role not found");
                }
                o.add("roles", newRoles);
                saveToFile(employees);
                return true;
            }
        }
        return false;
    }
    public List<Employee> fetchInActiveEmployee(){
    	 List<Employee> employees = new ArrayList<>();
         for (JsonElement el : getDataFromFile()) {
             JsonObject o = el.getAsJsonObject();
             if (!o.get("isActive").getAsBoolean()) {  
                 employees.add(new Employee(
                         o.get("id").getAsString(),
                         o.get("name").getAsString(),
                         o.get("dept").getAsString(),
                         o.get("dob").getAsString(),
                         o.get("address").getAsString(),
                         o.get("email").getAsString()
                 ));
             }
         }
         return employees;
    }
    public LoginResult validateUser(String id, String password) {
    	checkActiveEmployee(id);
        for (JsonElement el : getDataFromFile()) {
            JsonObject o = el.getAsJsonObject();
            String Id = o.get("id").getAsString();
            if (Id.equalsIgnoreCase(id)) {
                if (!EmployeeUtil.verify(password, o.get("password").getAsString())) {
                    System.out.println("Password mismatch.");
                    return new LoginResult(false, null, null);
                }    
                List<Roles> roles = new ArrayList<>();
                JsonArray arr = o.getAsJsonArray("roles");
                if (arr == null || arr.size() == 0) {
                    roles.add(Roles.USER);  
                } else {
                    for (JsonElement r : arr) {
                        roles.add(Roles.valueOf(r.getAsString().trim()));
                    }
                }
                return new LoginResult(true, id, roles);
            }
        }
		return null;
    }

	@Override
	public List<Employee> fetchInActiveEmployees() {
		 List<Employee> employees = new ArrayList<>();
	        for (JsonElement el : getDataFromFile()) {
	            JsonObject o = el.getAsJsonObject();
	            if (!o.get("isActive").getAsBoolean()) {  
	                employees.add(new Employee(
	                        o.get("id").getAsString(),
	                        o.get("name").getAsString(),
	                        o.get("dept").getAsString(),
	                        o.get("dob").getAsString(),
	                        o.get("address").getAsString(),
	                        o.get("email").getAsString()
	                ));
	            }
	        }
	        return employees;
	}
}
