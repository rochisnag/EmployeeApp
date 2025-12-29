package com.employee.dao;

import java.util.Scanner;
import com.google.gson.JsonObject;

public class UpdateName {
public static void updateName(JsonObject user, Scanner sc) {
		
	 System.out.print("Enter First Name: ");
     user.addProperty("fname", sc.nextLine());
        System.out.print("Enter Last Name: ");
        user.addProperty("lname", sc.nextLine());
    }
}
