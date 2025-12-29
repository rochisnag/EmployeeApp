package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonObject;

public class UpdateMail {
	 public static void updateMail(JsonObject user, Scanner sc) {
	        System.out.print("Enter Email: ");
	        user.addProperty("email", sc.nextLine());
	    }


}
