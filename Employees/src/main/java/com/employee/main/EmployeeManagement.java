package com.employee.main;

import com.employee.controller.MenuController;

public class EmployeeManagement {
	public static void main(String[] args) {
		String filepath;
		if (args.length > 0) {
			filepath = args[0];
		}
		else {
			filepath = "users.json";
		}
		MenuController.displayMenu(filepath);
	}
}
