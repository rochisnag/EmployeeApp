//package com.employee.daoDb;
//
//import com.employee.util.EmployeeUtil1;
//import com.employee.dao.EmployeeDao;
//import com.employee.model.Employee;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EmployeeDaoDbImpl1 implements EmployeeDao {
//	public void addEmployee(String name, String dept, String dob, String address, String email, List<String> rolesArray,
//			String hashPassword) {
//		String empSql = "INSERT INTO employee (id, name, dept, dob, address, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
//		String roleSql = "INSERT INTO employee_roles (emp_id, role) VALUES (?, ?)";
//		String empId = util.generateId();
//		try (Connection con = util.DBConnection()) {
//			con.setAutoCommit(false);
//			try (PreparedStatement empPs = con.prepareStatement(empSql);
//					PreparedStatement rolePs = con.prepareStatement(roleSql)) {
//				empPs.setString(1, empId);
//				empPs.setString(2, name);
//				empPs.setString(3, dept);
//				empPs.setDate(4, Date.valueOf(dob));
//				empPs.setString(5, address);
//				empPs.setString(6, email);
//				empPs.setString(7, hashPassword);
//				empPs.executeUpdate();
//				for (String role : rolesArray) {
//					rolePs.setString(1, empId);
//					rolePs.setString(2, role);
//					rolePs.executeUpdate();
//				}
//				con.commit();
//			}
//		} catch (Exception e) {
//			con.rollback();
//			e.printStackTrace();
//		}
//	}
//	public void updateEmployee(String id, String name, String dept, String dob, String address, String email) {
//		String sql = "UPDATE employee SET name=?, dept=?, dob=?, address=?, email=? WHERE id=?";
//		try (Connection con = util.DBConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//			ps.setString(1, name);
//			ps.setString(2, dept);
//			ps.setDate(3, Date.valueOf(dob));
//			ps.setString(4, address);
//			ps.setString(5, email);
//			ps.setString(6, id);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void deleteEmployee(String id) {
//		String sql = "DELETE FROM employee WHERE id=?";
//		try (Connection con = util.DBConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//			ps.setString(1, id);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void viewEmployee() {
//		String sql = "SELECT * FROM employee";
//		try (Connection con = util.DBConnection();
//				Statement st = con.createStatement();
//				ResultSet rs = st.executeQuery(sql)) {
//			while (rs.next()) {
//				System.out.println(rs.getString("id") + " | " + rs.getString("name") + " | " + rs.getString("dept"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void viewEmployee_by_id(String id) {
//		String sql = "SELECT * FROM employee WHERE id=?";
//		try (Connection con = util.DBConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//	    	ps.setString(1, id);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				System.out.println("Name  : " + rs.getString("name"));
//				System.out.println("Dept  : " + rs.getString("dept"));
//				System.out.println("Email : " + rs.getString("email"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void changePassword(String id, String oldPass, String newPass) {
//	    String checkSql = "SELECT password FROM employee WHERE id=?";
//	    String updateSql = "UPDATE employee SET password=? WHERE id=?";
//	    try (Connection con = util.DBConnection();
//	         PreparedStatement checkPs = con.prepareStatement(checkSql);
//	         PreparedStatement updatePs = con.prepareStatement(updateSql)) {
//	        checkPs.setString(1, id);
//	        ResultSet rs = checkPs.executeQuery();
//	        if (rs.next()) {
//	            String dbPassword = rs.getString("password");
//	            if (!dbPassword.equals(oldPass)) {
//	                System.out.println("Old password is incorrect");
//	                return;
//	            }
//	            updatePs.setString(1, newPass);
//	            updatePs.setString(2, id);
//	            updatePs.executeUpdate();
//	            System.out.println("Password changed successfully");
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
//	public void resetPassword(String id, String password) {
//	    String sql = "UPDATE employee SET password=? WHERE id=?";
//	    try (Connection con = util.DBConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//	        ps.setString(1, password);
//	        ps.setString(2, id);
//	        ps.executeUpdate();
//	        System.out.println("Password reset successfully");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}	
//	public void grantRole(String id, String role) {
//	    String sql = "INSERT INTO employee_roles (emp_id, role) VALUES (?, ?)";
//	    try (Connection con = util.DBConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//	        ps.setString(1, id);
//	        ps.setString(2, role);
//	        ps.executeUpdate();
//	        System.out.println("Role granted successfully");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
//	public void revokeRole(String id, String role) {
//	    String sql = "DELETE FROM employee_roles WHERE emp_id=? AND role=?";
//	    try (Connection con = util.DBConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//	        ps.setString(1, id);
//	        ps.setString(2, role);
//	        ps.executeUpdate();
//	        System.out.println("Role revoked successfully");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
//	public Employee getEmployeeById(String id) {
//	    String empSql = "SELECT * FROM employee WHERE id=?";
//	    String roleSql = "SELECT role FROM employee_roles WHERE emp_id=?";
//	    Employee emp = null;
//	    try (Connection con = util.DBConnection();
//	         PreparedStatement empPs = con.prepareStatement(empSql);
//	         PreparedStatement rolePs = con.prepareStatement(roleSql)) {
//	        empPs.setString(1, id);
//	        ResultSet rs = empPs.executeQuery();
//	        if (rs.next()) {
//	            emp = new Employee();
//	            emp.setId(rs.getString("id"));
//	            emp.setName(rs.getString("name"));
//	            emp.setDept(rs.getString("dept"));
//	            emp.setDob(rs.getDate("dob").toString());
//	            emp.setAddress(rs.getString("address"));
//	            emp.setEmail(rs.getString("email"));
//	            rolePs.setString(1, id);
//	            ResultSet roleRs = rolePs.executeQuery();
//	            List<String> roles = new ArrayList<>();
//	            while (roleRs.next()) {
//	                roles.add(roleRs.getString("role"));
//	            }
//	            emp.setRole(role);
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return emp;
//	}
//}
//
//	
//  
