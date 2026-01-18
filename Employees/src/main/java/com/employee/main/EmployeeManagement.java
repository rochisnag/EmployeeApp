package com.employee.main;
import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDbDaoImpl;
import com.employee.dao.EmployeeFileDaoImpl;
import com.employee.util.StorageTypes;
import java.util.Scanner;

public class EmployeeManagement {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.println("Select Storage? 1.FILE 2.POSTGRESQL");
		String choice=sc.next().toUpperCase();
		StorageTypes type=null;
		try {
			type=StorageTypes.valueOf(choice);
			MenuController menu = new MenuController();
			switch(type) {
				case FILE:
					menu.displayMenu(new EmployeeFileDaoImpl());
					break;
				case POSTGRESQL:
					menu.displayMenu(new EmployeeDbDaoImpl());
	     			break;				
			}
		}catch(IllegalArgumentException e) {
			System.out.println("invalid menu option");
		}
		
	}
}