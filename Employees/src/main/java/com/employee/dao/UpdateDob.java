package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonObject;

public class UpdateDob {
	 public static void updateDob(JsonObject user, Scanner sc) {
	        System.out.print("Enter DOB: ");
	        user.addProperty("dob", sc.nextLine());
	    }
}
