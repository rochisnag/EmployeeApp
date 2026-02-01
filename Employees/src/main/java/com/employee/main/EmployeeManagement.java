package com.employee.main;
import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDbDaoImpl;
import com.employee.dao.EmployeeFileDaoImpl;
import com.employee.util.StorageTypes;
import java.util.Scanner;


public class EmployeeManagement {
    public static void main(String[] args) {
        StorageTypes type = null;
        Scanner sc = new Scanner(System.in);
        while (type == null) {
            System.out.println("Select Storage Type:");
            for (StorageTypes types : StorageTypes.values()) {
                System.out.println(types);
            }
            System.out.println("Enter choice: ");
            String choice = sc.next().toUpperCase();
            try {
                type = StorageTypes.valueOf(choice);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid storage option");
            }
        }
        MenuController menu = new MenuController();
        switch (type) {
            case FILE:
                menu.displayMenu(new EmployeeFileDaoImpl());
                break;
            case POSTGRESQL:
                menu.displayMenu(new EmployeeDbDaoImpl());
                break;
        }
    }
}
