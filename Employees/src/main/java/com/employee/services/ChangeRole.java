package com.employee.services;
import java.util.Scanner;
import com.employee.dao.EmployeeDao;
import com.employee.daoFile.EmployeeDaoImpl;
import com.employee.util.Roles;
public class ChangeRole {
	 EmployeeDao dao = new EmployeeDaoImpl();
	private final Scanner sc = new Scanner(System.in);
	public void grantRole() {
		System.out.print("Enter Employee ID to grant role: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter Role to GRANT: ");
		String roleInput = sc.nextLine().trim().toUpperCase();
		try {
			Roles.valueOf(roleInput);
			dao.grantRole(id.toUpperCase(), roleInput);
			System.out.println("Role granted successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid role .Valid roles are:");
			for (Roles r : Roles.values()) {
				System.out.println("- " + r.name());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void revokeRole() {
		System.out.print("Enter Employee ID to revoke role: ");
		String id = sc.nextLine().trim().toUpperCase();
		System.out.print("Enter Role to revoke:");
		String roleInput = sc.nextLine().trim().toUpperCase();
		try {
			Roles.valueOf(roleInput);
			dao.revokeRole(id, roleInput);
			System.out.println("Role revoked successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid role. Valid roles are:");
			for (Roles r : Roles.values()) {
				System.out.println("- " + r.name());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
