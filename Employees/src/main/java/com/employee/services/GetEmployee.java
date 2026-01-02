package com.employee.services;

import java.io.File;
import java.util.Scanner;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.util.EmployeeUtil;

public class GetEmployee {

	private final EmployeeDao dao;
	private final File file;
	private final EmployeeUtil util;
	private final Scanner sc = new Scanner(System.in);

	public GetEmployee(String filepath) {
		this.file = new File(filepath);
		this.dao = new EmployeeDaoImpl(filepath);
		this.util = new EmployeeUtil(filepath);
	}

	public void get_all() {

		if (!file.exists() || file.length() <= 2) {
			System.out.println("No employee login");
			System.out.println();
			return;
		}
		dao.viewEmployee();
	}

	public void get_by_id() {

		try {
			String id;

			if ("USER".equals(CheckLogin.role)) {
				id = CheckLogin.id;
			} else {
				System.out.print("Enter emp id:");
				id = sc.next();
			}

			boolean present = util.checkEmployee(id);

			if (!present) {
				throw new EmployeeDoesNotExists("Employee doesnot  exist");
			}

			dao.viewEmployee_by_id(id);

		} catch (EmployeeDoesNotExists e) {
			System.out.println(e.getMessage());

		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
}
