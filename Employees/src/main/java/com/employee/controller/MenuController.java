package com.employee.controller;
import java.util.List;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.model.LoginResult;
import com.employee.util.Operations;
import com.employee.util.RolePermission;
import com.employee.util.Roles;
import com.employee.services.AddEmployee;
import com.employee.services.DeleteEmployee;
import com.employee.services.GetEmployee;
import com.employee.services.EmployeeLogin;
import com.employee.services.PasswordOperations;
import com.employee.services.UpdateEmployee;
import com.employee.services.ChangeEmpRole;

public class MenuController {
    public static LoginResult currentUser;
    public void displayMenu(EmployeeDao dao) {
       Scanner sc = new Scanner(System.in);
        AddEmployee addEmployee = new AddEmployee();
        UpdateEmployee updateEmployee = new UpdateEmployee();
        DeleteEmployee deleteEmployee = new DeleteEmployee();
        GetEmployee fetchEmployee = new GetEmployee();
        PasswordOperations passwordOperations = new PasswordOperations();
        EmployeeLogin validator = new EmployeeLogin();
        RolePermission rolePermission = new RolePermission();
        ChangeEmpRole changeEmpRole = new ChangeEmpRole();
        LoginResult login = validator.validate(dao);
        if (login == null)
            return;
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
            String input = sc.next().toUpperCase();
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
                    addEmployee.insert(dao);
                    break;
                case UPDATE:
                    updateEmployee.update(dao);
                    break;
                case DELETE:
                    deleteEmployee.delete(dao);
                    break;
                case FETCH:
                    fetchEmployee.getAll(dao);
                    break;
                case FETCH_EMPLOYEE_BY_ID:
                    fetchEmployee.getById(dao);
                    break;
                case RESETPASSWORD:
                    passwordOperations.resetPassword(dao);
                    break;
                case CHANGEPASSWORD:
                    passwordOperations.changePassword(dao);
                     break;
                case GRANT:
                	changeEmpRole.grantRole(dao);
                	break;
                case REVOKE:
                	changeEmpRole.revokeRole(dao);
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