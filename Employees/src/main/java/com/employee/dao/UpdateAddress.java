package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonObject;

public class UpdateAddress {
	 public static void updateAddress(JsonObject user, Scanner sc) {
	        System.out.print("Enter Address: ");
	        user.addProperty("address", sc.nextLine());
	    }
}
