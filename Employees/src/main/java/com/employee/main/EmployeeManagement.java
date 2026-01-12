package com.employee.main;
import com.employee.controller.MenuController;
import com.employee.util.Db;

public class EmployeeManagement {
	public static void main(String[] args) {
		Db.getConnection();
		MenuController.displayMenu();
	}
}
