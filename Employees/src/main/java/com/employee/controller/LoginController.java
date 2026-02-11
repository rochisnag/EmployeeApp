package com.employee.controller;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.ServiceException;
import com.employee.model.LoginResult;
import com.employee.services.LoginService;

public class LoginController {
    private final Scanner sc = new Scanner(System.in);
    private final LoginService service = new LoginService();
    public LoginResult login(EmployeeDao dao) {
        while (true) {
            try {
                System.out.print("Enter id: ");
                String id = sc.nextLine().trim().toUpperCase();
                System.out.print("Enter password: ");
                String password = sc.nextLine();
                LoginResult login = service.validate(id, password, dao);
                if (login != null && login.getSuccess()) {
                    System.out.println("Login successful!");
                    return login;  
                } 
            } catch (ServiceException | ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
