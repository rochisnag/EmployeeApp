package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonObject;

public class UpdateDept {
	 public static void updateDept(JsonObject user, Scanner sc) {
	        System.out.print("Enter Department: ");
	        user.addProperty("dept", sc.nextLine());
	    }
}
