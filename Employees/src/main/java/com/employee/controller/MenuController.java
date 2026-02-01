package com.employee.controller;
import java.util.List;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.model.LoginResult;
import com.employee.util.Operations;
import com.employee.util.RolePermission;
import com.employee.util.Roles;
import com.employee.services.EmployeeLogin;

public class MenuController {
  public static LoginResult currentUser;
  private final  Scanner sc = new Scanner(System.in);
  EmployeeController controller = new EmployeeController();
  EmployeeLogin validator = new EmployeeLogin();
  RolePermission rolePermission = new RolePermission();
  public void displayMenu(EmployeeDao dao) {  
    LoginController loginController = new LoginController();  
	LoginResult login = loginController.login(dao);
	currentUser = login;
	List<Roles> roles = login.getRoles();
        System.out.println("\nEMPLOYEE MANAGEMENT SYSTEM\n");
        while(true){
            System.out.println("AVAILABLE OPERATIONS:");
            for (Operations op : Operations.values()) {
                if (rolePermission.hasAccess(roles, op)) {
                    System.out.println(op);
                }
            }
            System.out.print("\nEnter choice: ");
            String input = sc.nextLine().trim().toUpperCase();
            Operations choice;
            try {
                choice = Operations.valueOf(input);
            }catch (IllegalArgumentException e) {
                System.out.println("Invalid choice");
                continue;
            }
            if (!rolePermission.hasAccess(roles, choice)) {
                System.out.println("Access Denied");
                continue;
            }
            switch(choice) {
                case ADD:
                    controller.addEmployee(dao); 
                    break;
                case UPDATE:
                    controller.update(dao);
                    break;
                case DELETE:
                    controller.delete(dao);
                    break;
                case FETCH:
                    controller.viewAllEmployees(dao);
                    break;
                case FETCH_EMPLOYEE_BY_ID:
                    controller.viewEmployeeById(dao);
                    break;
                case RESETPASSWORD:
                    controller.resetPassword(dao);
                    break;
                case CHANGEPASSWORD:
                    controller.changePassword(dao);
                     break;
                case GRANT:
                	controller.grantRole(dao);
                	break;
                case REVOKE:
                	controller.revokeRole(dao);
                	break;
                case EXIT:
                    System.out.println("EXIT...");
                    return;
                default:
                    System.out.println("Invalid operation");
            }
        }
    }
}