package com.employee.main;
import com.employee.controller.Menu;
import com.employee.dao.EmployeeDbDaoImpl;
import com.employee.dao.EmployeeFileDaoImpl;
import com.employee.util.StorageTypes;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import com.employee.dao.EmployeeDao;
import com.employee.util.DatabaseConfig;
public class EmployeeManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeDao  dao = null;
        while (dao == null) {
            System.out.println("Select Storage Type:");
            for (StorageTypes types : StorageTypes.values()) {
                System.out.println(types);
            }
            System.out.println("Enter choice: ");
            String choice = sc.next().toUpperCase();
            try {
               StorageTypes type = StorageTypes.valueOf(choice);
               switch (type) {
				case FILE:
					dao=new EmployeeFileDaoImpl();
					break;
				case DATABASE:
					DatabaseConfig.init(type);
					dao=new EmployeeDbDaoImpl();
					break;
				case SUPABASE:
					DatabaseConfig.init(type);
					dao=new EmployeeDbDaoImpl();
					break;
				}
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid storage option");
            }
        }
       
    	while(true) {
		Menu.displayMenu(dao);
	}
    }
}
