package com.employee.dao;

import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.employee.dao.Json;
import com.employee.exceptions.EmployeeDoesNotExists;

public class Privilege {
	
  public static void grant() {
	  try {
  
	   Scanner sc = new Scanner(System.in);
       JsonArray users = Json.loadUsers();

       System.out.print("Enter Employee ID: ");
       String id = sc.nextLine();

        System.out.print("Enter Role to GRANT: ");
        String newRole = sc.nextLine().toUpperCase();

        boolean found = false;
   
        for (JsonElement elem : users) {
            JsonObject user = elem.getAsJsonObject();

            if (user.get("id").getAsString().equals(id)) {
                found = true;

                JsonArray roles = user.getAsJsonArray("role");

                for (JsonElement r : roles) {
                    if (r.getAsString().equals(newRole)) {
                        System.out.println("User already has this role.");
                        return;
                    }
                }
                roles.add(newRole);
                user.add("role", roles);

                Json.saveUsers(users);
                System.out.println("Role granted successfully!");
                return;
            }
        }

        if (!found) {
            throw new EmployeeDoesNotExists("Employee ID '" + id + "' does not exist.");
        }
  }
     catch (EmployeeDoesNotExists e) {
        System.out.println(e.getMessage()); 
    } catch (Exception e) {
        System.out.println("An error occurred while revoking role.");
    }
  }
  

   
  public static void revoke() throws Exception {
   try {
	    Scanner sc = new Scanner(System.in);
	    JsonArray users = Json.loadUsers();

	    System.out.print("Enter Employee ID: ");
	    String id = sc.nextLine();

	    System.out.print("Enter Role to REVOKE: ");
	    String roleToRevoke = sc.nextLine().toUpperCase();

	    boolean found = false;

	    for (JsonElement elem : users) {
	        JsonObject user = elem.getAsJsonObject();

	        if (user.get("id").getAsString().equals(id)) {
	            found = true;

	            JsonArray roles = user.getAsJsonArray("role");
	            boolean hasRole = false;

	            // Iterate roles to find and remove
	            for (int i = 0; i < roles.size(); i++) {
	                if (roles.get(i).getAsString().equals(roleToRevoke)) {
	                    roles.remove(i);
	                    hasRole = true;
	                    break;
	                }
	            }

	            if (!hasRole) {
	                System.out.println("User does not have this role.");
	                return;
	            }

	            user.add("role", roles); // update user roles
	            Json.saveUsers(users);
	            System.out.println("Role revoked successfully!");
	            return;
	        }
	    }

	    if (!found) {
            throw new EmployeeDoesNotExists("Employee ID '" + id + "' does not exist.");
        }
   }
     catch (EmployeeDoesNotExists e) {
        System.out.println(e.getMessage()); 
    } catch (Exception e) {
        System.out.println("An error occurred while revoking role.");
    }
  }

}

