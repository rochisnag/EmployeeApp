package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.enums.UpdateChoice;
import com.employee.dao.Json;
import com.employee.dao.UpdateAddress;
import com.employee.dao.UpdateDob;
import com.employee.dao.UpdateName;
import com.employee.dao.UpdateDept;
import com.employee.dao.UpdateMail;

public class Update {

    public static void execute()  {

        Scanner sc = new Scanner(System.in);
        JsonArray users = Json.loadUsers(); // Load users using Gson

        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine().trim();

        JsonObject user = findUserById(users, id);

        if (user == null) {
            throw new EmployeeDoesNotExists("Employee ID '" + id + "' does not exist.");
        }

        System.out.println("\n--- UPDATE MENU ---");
        for (UpdateChoice c : UpdateChoice.values()) {
            System.out.println(c);
        }

        System.out.print("Enter your choice: ");
        UpdateChoice choice;

        try {
            choice = UpdateChoice.valueOf(sc.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid choice!");
            return;
        }

        switch (choice) {

            case NAME:
                UpdateName.updateName(user, sc);
                break;

            case DOB:
                UpdateDob.updateDob(user, sc);
                break;

            case DEPT:
                UpdateDept.updateDept(user, sc);
                break;

            case MAIL:
                UpdateMail.updateMail(user, sc);
                break;

            case ADDRESS:
                UpdateAddress.updateAddress(user, sc);
                break;

            case ALL:
                updateAll(user, sc);
                break;
        }

        Json.saveUsers(users);
        System.out.println("Update successful!");
    }

  
    private static JsonObject findUserById(JsonArray users, String id) {

        for (JsonElement elem : users) {
            JsonObject user = elem.getAsJsonObject();
            if (user.get("id").getAsString().equals(id)) {
                return user;
            }
        }
        return null;
    }

    // ================= UPDATE ALL FIELDS =================
    private static void updateAll(JsonObject user, Scanner sc) {
        UpdateName.updateName(user, sc);
        UpdateDob.updateDob(user, sc);
        UpdateDept.updateDept(user, sc);
        UpdateMail.updateMail(user, sc);
        UpdateAddress.updateAddress(user, sc);
    }
}
