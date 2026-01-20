package com.employee.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import com.employee.util.EmployeeUtil;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeeDbDaoImpl implements EmployeeDao {
	EmployeeUtil util = new EmployeeUtil();
	Connection conn = util.getConnection();

	public void addEmployee(String name, String dept, String dob, String address, String email, List<Roles> rolesArray,
			String hashPassword) {
		String insertEmployee = "INSERT INTO employees (emp_name, emp_dob, emp_address, emp_email, department_name )"
				+ "VALUES (?,?,?,?,?)";
		String insertLogin = "INSERT INTO emp_login (empId, emp_password) VALUES (?,?)";
		String insertRole = "INSERT INTO EmpRole (empId, empRole) VALUES (?,?)";
		try {
			conn.setAutoCommit(false);
			try (PreparedStatement empStmt = conn.prepareStatement(insertEmployee, Statement.RETURN_GENERATED_KEYS);) {
				empStmt.setString(1, name);
				empStmt.setDate(2, java.sql.Date.valueOf(dob));
				empStmt.setString(3, address);
				empStmt.setString(4, email);
				empStmt.setString(5, dept);
				empStmt.executeUpdate();
				ResultSet rs = empStmt.getGeneratedKeys();
				if (!rs.next()) {
					throw new SQLException("Employee ID not generated");
				}
				String generatedId = rs.getString(1);
				try (PreparedStatement loginStmt = conn.prepareStatement(insertLogin)) {
					loginStmt.setString(1, generatedId);
					loginStmt.setString(2, hashPassword);
					loginStmt.executeUpdate();
				}
				try (PreparedStatement roleStmt = conn.prepareStatement(insertRole)) {
					for (Roles role : rolesArray) {
						roleStmt.setString(1, generatedId);
						roleStmt.setObject(2, role.name(), java.sql.Types.OTHER);
						roleStmt.addBatch();
					}
					roleStmt.executeBatch();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateEmployee(String id, String name, String dept, String DOB, String address, String email,
			Roles role) {
		if (checkEmpExists(id)) {
			String userUpdateQuery = "update employees set emp_dob = ?, emp_address = ?, emp_email = ? where empId = ?";
			String adminUpdateQuery = "update employees set emp_name = ?, department_name = ?, emp_dob= ?, emp_address = ?, emp_email = ? where empId = ?";
			if (role.equals(Roles.USER)) {
				try {
					conn.setAutoCommit(false);
					PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date javaDate = sdf.parse(DOB);
					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
					userPstmt.setDate(1, sqlDate);
					userPstmt.setString(2, address);
					userPstmt.setString(3, email);
					userPstmt.setString(4, id);
					int row = userPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					conn.commit();
				} catch (SQLException e) {
					System.out.println("Error while updating " + e.getMessage());
					if (conn != null) {
						try {
							System.err.println("transaction has rollback : " + e.getMessage());
							conn.rollback();
						} catch (SQLException ex) {
							System.err.println("Error during rollback: " + ex.getMessage());
						}
					}
				} catch (ParseException e) {
					System.out.println("Error in formatting date" + e.getMessage());
				}
			} else {
				try {
					conn.setAutoCommit(false);
					PreparedStatement adminPstmt = conn.prepareStatement(adminUpdateQuery);
					adminPstmt.setString(1, name);
					adminPstmt.setString(2, dept);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date javaDate = sdf.parse(DOB);
					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
					adminPstmt.setDate(3, sqlDate);
					adminPstmt.setString(4, address);
					adminPstmt.setString(5, email);
					adminPstmt.setString(6, id);
					int row = adminPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					conn.commit();
				} catch (SQLException e) {
					System.out.println("Error while updating " + e.getMessage());
					if (conn != null) {
						try {
							System.err.println("transaction has rollback : " + e.getMessage());
							conn.rollback();
						} catch (SQLException ex) {
							System.err.println("Error during rollback: " + ex.getMessage());
						}
					}
				} catch (ParseException e) {
					System.out.println("Error in formatting date" + e.getMessage());
				}
			}
		}
	}
	public void deleteEmployee(String id) {
		if (!checkEmpExists(id))
			return;
		try {
			conn.setAutoCommit(false);
			try (PreparedStatement ps1 = conn.prepareStatement("delete from EmpRole where empId = ?");
					PreparedStatement ps2 = conn.prepareStatement("delete from emp_login where empId = ?");
					PreparedStatement ps3 = conn.prepareStatement("delete from employees where empId = ?")) {
				ps1.setString(1, id);
				ps2.setString(1, id);
				ps3.setString(1, id);
				ps1.executeUpdate();
				ps2.executeUpdate();
				int rows = ps3.executeUpdate();

				if (rows > 0) {
					conn.commit();
					System.out.println("Employee deleted successfully");
				} else {
					conn.rollback();
					System.out.println("Cannot delete employee");
				}
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception ignored) {
			}
			System.out.println("Error deleting from db " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void viewAllEmployee() {
		try {
			String viewAllQuery = "SELECT empId, emp_name, department_name, emp_dob, emp_address, emp_email FROM employees ORDER BY empId";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(viewAllQuery);
			while (rs.next()) {
				System.out.println();
				System.out.println("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("emp_name")
						+ " | Department: " + rs.getString("department_name") + " | DOB: " + rs.getDate("emp_dob")
						+ " | Address: " + rs.getString("emp_address") + " | Email: " + rs.getString("emp_email"));
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void viewEmployeeById(String id) {
		id = id.trim().toUpperCase();
		if (checkEmpExists(id)) {
			try {
				String viewQuery = "SELECT empId, emp_name, department_name, emp_dob, emp_address, emp_email "
						+ "FROM employees WHERE UPPER(empId) = ?";
				PreparedStatement pstmt = conn.prepareStatement(viewQuery);
				pstmt.setString(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					System.out.println();
					System.out.println("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("emp_name")
							+ " | Department: " + rs.getString("department_name") + " | DOB: " + rs.getDate("emp_dob")
							+ " | Address: " + rs.getString("emp_address") + " | Email: " + rs.getString("emp_email"));
				}
			} catch (SQLException e) {
				System.out.println("Error reading from database " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public void changePassword(String id, String oldPass, String newPass) {
	    String selectQuery = "SELECT emp_password FROM emp_login WHERE empId = ?";
	    String updateQuery = "UPDATE emp_login SET emp_password = ? WHERE empId = ?";
	    EmployeeUtil util = new EmployeeUtil();
	    try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
	        selectStmt.setString(1, id);
	        ResultSet rs = selectStmt.executeQuery();
	        if (!rs.next()) {
	            System.out.println("Employee not found");
	            return;
	        }
	        String dbPassword = rs.getString("emp_password"); 
            if (!util.verify(oldPass, dbPassword)) {
	            System.out.println("Old password incorrect");
	            return;
	        }
            String hashedNewPass = util.hash(newPass);
	        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
	            updateStmt.setString(1, hashedNewPass);
	            updateStmt.setString(2, id);           
	            int rows = updateStmt.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Password changed successfully");
	            } else {
	                System.out.println("Failed to update password");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	public void resetPassword(String id, String password) {
		String resetPassQuery = "update emp_login set emp_password = ? where empId = ?";
		if (checkEmpExists(id)) {
			try {
				PreparedStatement pstmt = conn.prepareStatement(resetPassQuery);
				pstmt.setString(1, password);
				pstmt.setString(2, id);
				int row = pstmt.executeUpdate();
				if (row != 0) {
					System.out.println("Successfully reset password");
				} else {
					System.out.println("Cannot reset emplpoyee password");
				}
			} catch (SQLException e) {
				System.out.println("Error reseting password " + e.getMessage());
			}
		}
	}

	public void grantRole(String id, String role) {
		String grantRoleQuery = "INSERT INTO EmpRole (empid, emprole) VALUES (?, ?)";

		if (!checkEmpExists(id)) {
			System.out.println("Employee doesn't exist");
			return;
		}

		if (checkRoleExists(id, role)) {
			System.out.println("Role already exists");
			return;
		}
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(grantRoleQuery);
			pstmt.setString(1, id);
			pstmt.setObject(2, role, java.sql.Types.OTHER);
			int row = pstmt.executeUpdate();

			conn.commit();
			if (row != 0) {
				System.out.println("Successfully Granted Role");
			} else {
				System.out.println("Cannot grant role");
			}

		} catch (SQLException e) {
			System.out.println("Error in granting role: " + e.getMessage());
			try {
				conn.rollback();
				System.err.println("Transaction rolled back due to error");
			} catch (SQLException ex) {
				System.err.println("Error during rollback: " + ex.getMessage());
			}
		}
	}

	public void revokeRole(String id, String role) {
		String revokeRoleQuery = "DELETE FROM EmpRole WHERE empid = ? AND emprole = ?";
		if (!checkEmpExists(id)) {
			System.out.println("Employee doesn't exist");
			return;
		}
		if (!checkRoleExists(id, role)) {
			System.out.println("Role doesn't exist for this employee");
			return;
		}
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(revokeRoleQuery);
			pstmt.setString(1, id);
			pstmt.setObject(2, role, java.sql.Types.OTHER);
			int row = pstmt.executeUpdate();
			conn.commit();
			if (row != 0) {
				System.out.println("Successfully Revoked Role");
			} else {
				System.out.println("Cannot revoke role");
			}
		} catch (SQLException e) {
			System.out.println("Error in revoking role: " + e.getMessage());
			try {
				conn.rollback();
				System.err.println("Transaction rolled back due to error");
			} catch (SQLException ex) {
				System.err.println("Error during rollback: " + ex.getMessage());
			}
		}
	}

	private boolean checkEmpExists(String checkId) {
		checkId = checkId.trim().toUpperCase();
		String checkEmpQuery = "SELECT empId FROM employees WHERE UPPER(empId) = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(checkEmpQuery);
			pstmt.setString(1, checkId);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Employee doesn't exist");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db: " + e.getMessage());
		}
		return true;
	}
	private boolean checkRoleExists(String id, String role) {
		String checkRoleQuery = "select empRole from EmpRole where empId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(checkRoleQuery);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(role)) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
	}
	public LoginResult validateUser(String id, String password) {
		String authQuery = "select emp_password from emp_login where empId=?";
		String roleQuery = "select empRole from EmpRole where empId=?";
		try ( PreparedStatement ps = conn.prepareStatement(authQuery)) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return new LoginResult(false, null, null);
			String dbPass = rs.getString("emp_password");
			 if (!util.verify(password, dbPass)) {
		            return new LoginResult(false, null, null);
		        }
			List<Roles> roles = new ArrayList<>();
			try (PreparedStatement ps2 = conn.prepareStatement(roleQuery)) {
				ps2.setString(1, id);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					roles.add(Roles.valueOf(rs2.getString("empRole")));
				}
			}
			return new LoginResult(true, id, roles);
		} catch (SQLException e) {
			System.out.println("Error login" + e.getMessage());
		}
		return new LoginResult(false, null, null);
	}
}
