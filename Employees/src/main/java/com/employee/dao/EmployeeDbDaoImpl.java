//package com.employee.dao;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import com.employee.exceptions.DataAccessException;
//import com.employee.exceptions.EmployeeDoesNotExists;
//import com.employee.model.Employee;
//import com.employee.model.LoginResult;
//import com.employee.util.EmployeeUtil;
//import com.employee.util.Roles;
//import com.employee.exceptions.LoginFailedException;
//
//public class EmployeeDbDaoImpl implements EmployeeDao {
//	private final EmployeeUtil util = new EmployeeUtil();
//	Connection conn = util.getConnection();
//	public void addEmployee(Employee emp) {
//		String insertEmp = "insert into employees (emp_name, emp_dob, emp_address, emp_email, department_name) values (?, ?, ?, ?, ?)";
//		String insertLogin = "insert into emp_login (empid, emp_password) values (?, ?)";
//		String insertRole = "insert into emprole (empid, emprole) values (?, ?)";
//		try (Connection conn = util.getConnection()) {
//			conn.setAutoCommit(false);
//			try (PreparedStatement empStmt = conn.prepareStatement(insertEmp, Statement.RETURN_GENERATED_KEYS);
//					PreparedStatement loginStmt = conn.prepareStatement(insertLogin);
//					PreparedStatement roleStmt = conn.prepareStatement(insertRole)) {
//				empStmt.setString(1, emp.getName());
//				empStmt.setDate(2, Date.valueOf(emp.getDob()));
//				empStmt.setString(3, emp.getAddress());
//				empStmt.setString(4, emp.getEmail());
//				empStmt.setString(5, emp.getDept());
//				empStmt.executeUpdate();
//				try (ResultSet rs = empStmt.getGeneratedKeys()) {
//					if (!rs.next()) {
//						throw new DataAccessException("Failed to generate employee ID");
//					}
//					String empId = rs.getString(1);
//					loginStmt.setString(1, empId);
//					loginStmt.setString(2, emp.getPassword());
//					loginStmt.executeUpdate();
//					List<Roles> roles = emp.getRoles();
//					if (roles != null) {
//						for (Roles role : roles) {
//							roleStmt.setString(1, empId);
//							roleStmt.setObject(2, role, java.sql.Types.OTHER);
//							roleStmt.addBatch();
//						}
//						roleStmt.executeBatch();
//					}
//				}
//				conn.commit();
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error adding employee");
//		}
//	}
//
//	public boolean updateEmployee(Employee emp, Roles role) {
//		if (!checkEmpExists(emp.getId())) {
//			throw new EmployeeDoesNotExists("Employee does not exist with ID: " + emp.getId());
//		}
//		String userUpdate = "update employees set emp_dob=?, emp_address=?, emp_email=? where empId=?";
//		String adminUpdate = "update employees set emp_name=?, department_name=?, emp_dob=?, emp_address=?, emp_email=? where empId=?";
//		try (Connection conn = util.getConnection()) {
//			conn.setAutoCommit(false);
//			try (PreparedStatement pstmt = conn.prepareStatement(role.equals(Roles.USER) ? userUpdate : adminUpdate)) {
//				pstmt.setDate(1, Date.valueOf(emp.getDob()));
//				pstmt.setString(2, emp.getAddress());
//				pstmt.setString(3, emp.getEmail());
//				pstmt.setString(4, emp.getId());
//				if (!role.equals(Roles.USER)) {
//					pstmt.setString(1, emp.getName());
//					pstmt.setString(2, emp.getDept());
//					pstmt.setDate(3, Date.valueOf(emp.getDob()));
//					pstmt.setString(4, emp.getAddress());
//					pstmt.setString(5, emp.getEmail());
//					pstmt.setString(6, emp.getId());
//				}
//				int rows = pstmt.executeUpdate();
//				if (rows == 0) {
//					throw new DataAccessException("Update failed for employee: " + emp.getId());
//				}
//				conn.commit();
//				return true;
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error updating employee");
//		}
//	}
//
//	public boolean deleteEmployee(String id) {
//		if (!checkEmpExists(id)) {
//			return false;
//		}
//		String delRole = "delete from EmpRole where empId=?";
//		String delLogin = "delete from emp_login where empId=?";
//		String delEmp = "delete from employees where empId=?";
//		try (Connection conn = util.getConnection()) { 
//			conn.setAutoCommit(false);
//			try (PreparedStatement ps1 = conn.prepareStatement(delRole);
//					PreparedStatement ps2 = conn.prepareStatement(delLogin);
//					PreparedStatement ps3 = conn.prepareStatement(delEmp)) {
//				ps1.setString(1, id);
//				ps2.setString(1, id);
//				ps3.setString(1, id);
//				ps1.executeUpdate();
//				ps2.executeUpdate();
//				int rows = ps3.executeUpdate();
//				conn.commit();
//				return rows > 0;
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error deleting employee");
//		}
//	}
//	public List<Employee> viewAllEmployee() {
//		String query = "select empid, emp_name, department_name, emp_dob, emp_address, emp_email from employees";
//		List<Employee> employees = new ArrayList<>();
//		try (Connection conn = util.getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(query);
//				ResultSet rs = pstmt.executeQuery()) {
//			while (rs.next()) {
//				Employee emp = new Employee(rs.getString("empid"), rs.getString("emp_name"),
//						rs.getString("department_name"), rs.getDate("emp_dob").toLocalDate().toString(),
//						rs.getString("emp_address"), rs.getString("emp_email"));
//				employees.add(emp);
//			}
//			return employees;
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error retrieving all employees");
//		}
//	}
//
//	public Employee viewEmployeeById(String id) {
//		String query = "select empid, emp_name, department_name, emp_dob, emp_address, emp_email "
//				+ "from employees where upper(empid)=?";
//		try (Connection conn = util.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
//			pstmt.setString(1, id.toUpperCase());
//			try (ResultSet rs = pstmt.executeQuery()) {
//				if (rs.next()) {
//					return new Employee(rs.getString("empid"), rs.getString("emp_name"),
//							rs.getString("department_name"), rs.getDate("emp_dob").toLocalDate().toString(),
//							rs.getString("emp_address"), rs.getString("emp_email"));
//				} 
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error retrieving employee by id");
//		}
//		return null;
//	}
//	public boolean changePassword(String id, String oldPass, String newPass) {
//		String selectQuery = "select emp_password from emp_login where empid = ?";
//		String updateQuery = "update emp_login set emp_password = ? where empid = ?";
//		id = id.trim().toUpperCase();
//		if (!checkEmpExists(id)) {
//			throw new EmployeeDoesNotExists("Employee does not exist with id: "+ id);
//		}
//		EmployeeUtil util = new EmployeeUtil();
//		String dbPassword;
//		try (PreparedStatement selectStmt = util.getConnection().prepareStatement(selectQuery)) {
//			selectStmt.setString(1, id);
//			try (ResultSet rs = selectStmt.executeQuery()) {
//				if (!rs.next()) {
//					throw new DataAccessException("Failed to retrieve password for employee: " + id);
//				}
//				dbPassword = rs.getString("emp_password");
//			}
//		} catch (SQLException e) {
//			throw new DataAccessException("Error reading password from database");
//		}
//		if (!util.verify(oldPass, dbPassword)) {
//			throw new DataAccessException("Old password is incorrect for employee: " + id);
//		}
//		String hashedNewPass = util.hash(newPass);
//		try (PreparedStatement updateStmt = util.getConnection().prepareStatement(updateQuery)) {
//			updateStmt.setString(1, hashedNewPass);
//			updateStmt.setString(2, id);
//			int rowsAffected = updateStmt.executeUpdate();
//			if (rowsAffected == 0) {
//				throw new DataAccessException("Password update failed for employee: " + id);
//			}
//			return true;
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error updating password in database");
//		}
//	}
//
//	public boolean resetPassword(String id, String password) {
//		if (!checkEmpExists(id)) {
//			throw new EmployeeDoesNotExists("Employee does not exist with id: " + id);
//		}
//		String q = "update emp_login set emp_password=? where empid=?";
//		try (Connection conn = util.getConnection(); PreparedStatement pstmt = conn.prepareStatement(q)) {
//			pstmt.setString(1, password);
//			pstmt.setString(2, id);
//			int rows = pstmt.executeUpdate();
//			if (rows == 0) {
//				throw new DataAccessException("No login exists for employee: " + id);
//			}
//			return true;
//
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error resetting password");
//		}
//	}
//	public boolean grantRole(String id, Roles role) {
//		if (!checkEmpExists(id)) {
//			throw new EmployeeDoesNotExists("Employee does not exist with id: " + id);
//		}
//		if (checkRoleExists(id, role)) {
//			throw new DataAccessException("Role '" + role + "' already exists for employee: " + id);
//		}
//		String q = "insert into emprole (empid, emprole) values (?, ?)";
//		try (Connection conn = util.getConnection(); PreparedStatement pstmt = conn.prepareStatement(q)) {
//			pstmt.setString(1, id);
//			pstmt.setObject(2, role, java.sql.Types.OTHER);
//			int rows = pstmt.executeUpdate();
//			if (rows == 0) {
//				throw new DataAccessException("Role not granted to employee: " + id);
//			}
//			return true;
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error granting role");
//		}
//	}
//	public boolean revokeRole(String id, Roles role) {
//		if (!checkEmpExists(id)) {
//			throw new EmployeeDoesNotExists("Employee does not exist with id: "+ id);
//		}
//		if (!checkRoleExists(id, role)) {
//			throw new DataAccessException("Role '" + role + "' does not exist for employee: " + id);
//		}
//		String q = "delete from emprole where empid=? and emprole=?";
//		try (Connection conn = util.getConnection(); PreparedStatement pstmt = conn.prepareStatement(q)) {
//
//			pstmt.setString(1, id);
//			pstmt.setObject(2, role, java.sql.Types.OTHER);
//
//			int rows = pstmt.executeUpdate();
//			if (rows == 0) {
//				throw new DataAccessException("Role not revoked for employee: " + id);
//			}
//			return true;
//
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error revoking role");
//		}
//	}
//	private boolean checkEmpExists(String checkId) {
//		checkId = checkId.trim().toUpperCase();
//		String checkEmpQuery = "select empId FROM employees where upper(empId) = ?";
//		try {
//			PreparedStatement pstmt = conn.prepareStatement(checkEmpQuery);
//			pstmt.setString(1, checkId);
//			ResultSet rs = pstmt.executeQuery();
//			if (!rs.next()) {
//				System.out.println("Employee doesn't exists");
//				return false;
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error checking emp in db: ");
//		}
//		return true;
//	}
//	private boolean checkRoleExists(String id, Roles role) {
//		String checkRoleQuery = "select empRole from EmpRole where empId = ?";
//		try {
//			PreparedStatement pstmt = conn.prepareStatement(checkRoleQuery);
//			pstmt.setString(1, id);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				if (rs.getString(1).equals(role)) {
//					return true;
//				}
//			}
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//		throw new DataAccessException("Error checking emp in db");
//		}
//		return false;
//	}
//	public LoginResult validateUser(String id, String password) {
//		String authQ = "select emp_password from emp_login where empid=?";
//		String roleQ = "select emprole from EmpRole where empid=?";
//		try (Connection conn = util.getConnection();
//				PreparedStatement auth = conn.prepareStatement(authQ);
//				PreparedStatement roleStmt = conn.prepareStatement(roleQ)) {
//			auth.setString(1, id);
//			try (ResultSet rs = auth.executeQuery()) {
//				if (rs.next()) {  
//					String dbPassword = rs.getString("emp_password");
//	                if (!util.verify(password, dbPassword)) {  
//	                    throw new LoginFailedException("invalid credentials");
//	                }
//			}
//			}
//			List<Roles> roles = new ArrayList<>();
//			roleStmt.setString(1, id);
//			try (ResultSet rs2 = roleStmt.executeQuery()) {
//				while (rs2.next()) {
//					roles.add(Roles.valueOf(rs2.getString("emprole")));
//				}
//			}
//			return new LoginResult(true, id, roles);
//		} catch (SQLException e) {
//			System.out.println("SQL Exception occured"+e.getMessage());
//			throw new DataAccessException("Error validating user");
//		}
//	}
//}
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

    // -------------------- Helper Methods --------------------

    private void checkEmpExists(String id) {
        String query = "SELECT 1 FROM employees WHERE upper(empid) = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EmployeeDoesNotExists("Employee does not exist with ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking employee existence", e);
        }
    }

    private boolean checkRoleExists(String id, Roles role) {
        String query = "SELECT emprole FROM emprole WHERE empid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString(1).equalsIgnoreCase(role.name())) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking role existence", e);
        }
        return false;
    }

    // -------------------- Employee CRUD --------------------

    @Override
    public void addEmployee(Employee emp) {
        String insertEmp = "INSERT INTO employees (emp_name, emp_dob, emp_address, emp_email, department_name) VALUES (?, ?, ?, ?, ?)";
        String insertLogin = "INSERT INTO emp_login (empid, emp_password) VALUES (?, ?)";
        String insertRole = "INSERT INTO emprole (empid, emprole) VALUES (?, ?)";

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
                            roleStmt.setString(2, role.name());
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
            throw new DataAccessException("Error adding employee", e);
        }
    }

    @Override
    public boolean updateEmployee(Employee emp, Roles role) {
        checkEmpExists(emp.getId());

        String userUpdate = "UPDATE employees SET emp_dob=?, emp_address=?, emp_email=? WHERE empid=?";
        String adminUpdate = "UPDATE employees SET emp_name=?, department_name=?, emp_dob=?, emp_address=?, emp_email=? WHERE empid=?";

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
            throw new DataAccessException("Error updating employee", e);
        }
    }

    @Override
    public boolean deleteEmployee(String id) {
        checkEmpExists(id);

        String delRole = "DELETE FROM emprole WHERE empid=?";
        String delLogin = "DELETE FROM emp_login WHERE empid=?";
        String delEmp = "DELETE FROM employees WHERE empid=?";

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
            throw new DataAccessException("Error deleting employee", e);
        }
    }

    @Override
    public List<Employee> viewAllEmployee() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT empid, emp_name, department_name, emp_dob, emp_address, emp_email FROM employees";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getString("empid"),
                        rs.getString("emp_name"),
                        rs.getString("department_name"),
                        rs.getDate("emp_dob").toLocalDate().toString(),
                        rs.getString("emp_address"),
                        rs.getString("emp_email")
                ));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving employees", e);
        }

        return employees;
    }

    @Override
    public Employee viewEmployeeById(String id) {
        checkEmpExists(id);

        String query = "SELECT empid, emp_name, department_name, emp_dob, emp_address, emp_email FROM employees WHERE upper(empid)=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getString("empid"),
                            rs.getString("emp_name"),
                            rs.getString("department_name"),
                            rs.getDate("emp_dob").toLocalDate().toString(),
                            rs.getString("emp_address"),
                            rs.getString("emp_email")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving employee", e);
        }

        return null;
    }

    // -------------------- Password Methods --------------------

    @Override
    public boolean changePassword(String id, String oldPass, String newPass) {
        checkEmpExists(id);

        String selectQ = "SELECT emp_password FROM emp_login WHERE empid=?";
        String updateQ = "UPDATE emp_login SET emp_password=? WHERE empid=?";
        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQ)) {

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
            throw new DataAccessException("Error changing password", e);
        }
    }

    @Override
    public boolean resetPassword(String id, String password) {
        checkEmpExists(id);

        String query = "UPDATE emp_login SET emp_password=? WHERE empid=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, password);
            pstmt.setString(2, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error resetting password", e);
        }
    }

    // -------------------- Role Methods --------------------

    @Override
    public boolean grantRole(String id, Roles role) {
        checkEmpExists(id);

        if (checkRoleExists(id, role)) return false;

        String query = "INSERT INTO emprole (empid, emprole) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.setString(2, role.name());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error granting role", e);
        }
    }

    @Override
    public boolean revokeRole(String id, Roles role) {
        checkEmpExists(id);

        if (!checkRoleExists(id, role)) {
            throw new DataAccessException("Role not found");
        }

        String query = "DELETE FROM emprole WHERE empid=? AND emprole=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.setString(2, role.name());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error revoking role", e);
        }
    }

    // -------------------- Authentication --------------------

    @Override
    public LoginResult validateUser(String id, String password) {
        String authQ = "SELECT emp_password FROM emp_login WHERE empid=?";
        String roleQ = "SELECT emprole FROM emprole WHERE empid=?";

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

            if (roles.isEmpty()) roles.add(Roles.USER);
            return new LoginResult(true, id, roles);

        } catch (SQLException e) {
            throw new DataAccessException("Error validating user", e);
        }
    }
}

