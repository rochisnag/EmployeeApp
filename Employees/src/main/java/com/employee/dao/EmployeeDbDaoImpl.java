package com.employee.dao;

import com.employee.util.EmployeeUtil;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.model.LoginResult;
import com.employee.util.Roles;
import com.employee.model.Employee;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDbDaoImpl implements EmployeeDao {
	private final EmployeeUtil util = new EmployeeUtil();

	private Connection getConnection() {
		return util.getConnection();
	}

	private void checkEmpExists(String id) {
		String query = "select 1 from employees where upper(empId) = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id.trim().toUpperCase());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					throw new EmployeeDoesNotExists("Employee does not exist with ID: " + id);
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error checking employee existence", e);
		}
	}

	private boolean checkRoleExists(String id, Roles role) {
		String query = "select empRole from EmpRole where empId = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					if (rs.getString(1).equalsIgnoreCase(role.name())) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error checking role existence", e);
		}
		return false;
	}

	public void addEmployee(Employee emp) {
		String insertEmp = "insert into employees (emp_name, emp_dob, emp_address, emp_email, department_name) values (?, ?, ?, ?, ?)";
		String insertLogin = "insert into emp_login (empid, emp_password) values (?, ?)";
		String insertRole = "insert into emprole (empid, emprole) values (?, ?)";
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement empStmt = conn.prepareStatement(insertEmp, Statement.RETURN_GENERATED_KEYS);
					PreparedStatement loginStmt = conn.prepareStatement(insertLogin);
					PreparedStatement roleStmt = conn.prepareStatement(insertRole)) {
				empStmt.setString(1, emp.getName());
				empStmt.setDate(2, Date.valueOf(emp.getDob()));
				empStmt.setString(3, emp.getAddress());
				empStmt.setString(4, emp.getEmail());
				empStmt.setString(5, emp.getDept());
				empStmt.executeUpdate();
				try (ResultSet rs = empStmt.getGeneratedKeys()) {
					if (!rs.next()) {
						throw new DataAccessException("Failed to generate employee ID");
					}
					String empId = rs.getString(1);
					loginStmt.setString(1, empId);
					loginStmt.setString(2, emp.getPassword());
					loginStmt.executeUpdate();

					if (emp.getRoles() != null) {
						for (Roles role : emp.getRoles()) {
							roleStmt.setString(1, empId);
							roleStmt.setObject(2, role, java.sql.Types.OTHER);
							roleStmt.addBatch();
						}
						roleStmt.executeBatch();
					}
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error adding employee", e);
		}
	}

	public boolean updateEmployee(Employee emp, Roles role) {
		checkEmpExists(emp.getId());
		String userUpdate = "update employees set emp_dob=?, emp_address=?, emp_email=? where empId=?";
		String adminUpdate = "update employees set emp_name=?, department_name=?, emp_dob=?, emp_address=?, emp_email=? where empId=?";
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(role.equals(Roles.USER) ? userUpdate : adminUpdate)) {
			if (role.equals(Roles.USER)) {
				pstmt.setDate(1, Date.valueOf(emp.getDob()));
				pstmt.setString(2, emp.getAddress());
				pstmt.setString(3, emp.getEmail());
				pstmt.setString(4, emp.getId());
			} else {
				pstmt.setString(1, emp.getName());
				pstmt.setString(2, emp.getDept());
				pstmt.setDate(3, Date.valueOf(emp.getDob()));
				pstmt.setString(4, emp.getAddress());
				pstmt.setString(5, emp.getEmail());
				pstmt.setString(6, emp.getId());
			}
			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error updating employee", e);
		}
	}

	public boolean deleteEmployee(String id) {
		checkEmpExists(id);
		String delRole = "delete from EmpRole where empId=?";
		String delLogin = "delete from emp_login where empId=?";
		String delEmp = "delete from employees where empId=?";
		try (Connection conn = getConnection();
				PreparedStatement ps1 = conn.prepareStatement(delRole);
				PreparedStatement ps2 = conn.prepareStatement(delLogin);
				PreparedStatement ps3 = conn.prepareStatement(delEmp)) {
			ps1.setString(1, id);
			ps2.setString(1, id);
			ps3.setString(1, id);
			ps1.executeUpdate();
			ps2.executeUpdate();
			int rows = ps3.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error deleting employee", e);
		}
	}

	public List<Employee> viewAllEmployee() {
		List<Employee> employees = new ArrayList<>();
		String query = "select empid, emp_name, department_name, emp_dob, emp_address, emp_email from employees";
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				employees.add(new Employee(rs.getString("empid"), rs.getString("emp_name"),
						rs.getString("department_name"), rs.getDate("emp_dob").toLocalDate().toString(),
						rs.getString("emp_address"), rs.getString("emp_email")));
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error retrieving employees", e);
		}
		return employees;
	}

	public Employee viewEmployeeById(String id) {
		checkEmpExists(id);
		String query = "select empid, emp_name, department_name, emp_dob, emp_address, emp_email "
				+ "from employees where upper(empid)=?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id.trim().toUpperCase());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Employee(rs.getString("empid"), rs.getString("emp_name"),
							rs.getString("department_name"), rs.getDate("emp_dob").toLocalDate().toString(),
							rs.getString("emp_address"), rs.getString("emp_email"));
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error retrieving employee", e);
		}
		return null;
	}

	public boolean changePassword(String id, String oldPass, String newPass) {
		checkEmpExists(id);
		String selectQ = "select emp_password from emp_login where empid = ?";
		String updateQ = "update emp_login set emp_password = ? where empid = ?";
		try (Connection conn = getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectQ)) {
			selectStmt.setString(1, id);
			String dbPassword = null;
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					dbPassword = rs.getString("emp_password");
				}
			}
			if (!util.verify(oldPass, dbPassword)) {
				throw new DataAccessException("Old password incorrect");
			}
			try (PreparedStatement updateStmt = conn.prepareStatement(updateQ)) {
				updateStmt.setString(1, util.hash(newPass));
				updateStmt.setString(2, id);
				int rows = updateStmt.executeUpdate();
				return rows > 0;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error changing password", e);
		}
	}

	public boolean resetPassword(String id, String password) {
		checkEmpExists(id);
		String query = "update emp_login set emp_password=? where empid=?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.setString(2, id);
			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error resetting password", e);
		}
	}

	public boolean grantRole(String id, Roles role) {
		checkEmpExists(id);
		if (checkRoleExists(id, role))
			return false;
		String query = "insert into emprole (empid, emprole) values (?, ?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			pstmt.setObject(2, role, java.sql.Types.OTHER);
			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error granting role", e);
		}
	}

	public boolean revokeRole(String id, Roles role) {
		checkEmpExists(id);
		if (!checkRoleExists(id, role)) {
			throw new DataAccessException("Role not found");
		}
		String query = "delete from emprole where empid=? and emprole=?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			pstmt.setObject(2, role, java.sql.Types.OTHER);
			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error revoking role", e);
		}
	}

	public LoginResult validateUser(String id, String password) {
		checkEmpExists(id);
		String authQ = "select emp_password from emp_login where empid=?";
		String roleQ = "select emprole from EmpRole where empid=?";
		try (Connection conn = getConnection();
				PreparedStatement auth = conn.prepareStatement(authQ);
				PreparedStatement roleStmt = conn.prepareStatement(roleQ)) {
			auth.setString(1, id);
			try (ResultSet rs = auth.executeQuery()) {
				if (rs.next()) {
					String dbPassword = rs.getString("emp_password");
					if (!util.verify(password, dbPassword)) {
						return new LoginResult(false, null, null);
					}
				} else {
					throw new EmployeeDoesNotExists("Employee not found with ID: " + id);
				}
			}
			List<Roles> roles = new ArrayList<>();
			roleStmt.setString(1, id);
			try (ResultSet rs = roleStmt.executeQuery()) {
				while (rs.next()) {
					roles.add(Roles.valueOf(rs.getString("emprole")));
				}
			}
			if (roles.isEmpty())
				roles.add(Roles.USER);
			return new LoginResult(true, id, roles);
		} catch (SQLException e) {
			System.out.println("SQL Exception occured" + e.getMessage());
			throw new DataAccessException("Error validating user", e);
		}
	}
}
