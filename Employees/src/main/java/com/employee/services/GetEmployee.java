package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.daoFile.ServerSideValidations;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.util.EmployeeUtil;
public class GetEmployee {	
    EmployeeDao dao = new EmployeeDaoImpl();
    EmployeeUtil util = new EmployeeUtil();
    ServerSideValidations se = new ServerSideValidations();
	private final  Scanner sc = new Scanner(System.in);
    public void get_all() {
		dao.viewEmployee();
	}
	public void get_by_id() {
		try {
			String id;
			if ("USER".equals(se.role)) {
				id = ServerSideValidations.id;
			} else {
				System.out.print("Enter emp id:");
				id = sc.next();
			}
			boolean present = se.checkEmpExists(id.toUpperCase());
			if (!present) {
				throw new EmployeeDoesNotExists("Employee doesnot  exist");
			}
			dao.viewEmployee_by_id(id.toUpperCase());
		} catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
}
