package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeFileDaoImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import com.employee.controller.MenuController;
public class GetEmployee {	
    EmployeeDao dao = new EmployeeFileDaoImpl();
    EmployeeUtil util = new EmployeeUtil();
    ServerSideValidations se = new ServerSideValidations();
	private final  Scanner sc = new Scanner(System.in);
    public void get_all(EmployeeDao dao) {
		dao.viewEmployee();
	}
	
    public void get_by_id(EmployeeDao dao) {
        try {
            if (MenuController.currentUser.getRoles().contains(Roles.ADMIN)
                    || MenuController.currentUser.getRoles().contains(Roles.MANAGER)) {

                System.out.println("Enter emp id:");
                String id = sc.nextLine();

                dao.viewEmployee_by_id(id);

            } else {
                System.out.println("Access denied");
            }

        } catch (EmployeeDoesNotExists e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
