package com.employee.daoDb;
import com.employee.util.Db;
import com.employee.model.Employee;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class EmployeeDaoDbImpl{
	ServerSideValidations se = new ServerSideValidations();
	public void addEmployee(String name, String dept, String dob, String address,String email, List<String> rolesArray,
			String hashPassword) {
		String empId = se.IdAutogenerate();
		String empSql = "insert into users (id, name, dept, dob, address, email, password) values(?, ?, ?, ?, ?, ?, ?)";
		String roleSql = "insert into user_role(user_id,role_name) values (?, ?)";
		try (Connection con = Db.getConnection()) {
			try (PreparedStatement empPs = con.prepareStatement(empSql);PreparedStatement rolePs = con.prepareStatement(roleSql)) {
				empPs.setString(1, empId);
				empPs.setString(2, name);
				empPs.setString(3, dept);
				empPs.setString(4, dob);
				empPs.setString(5, address);
				empPs.setString(6, email);
				empPs.setString(7, hashPassword);
				empPs.executeUpdate();
				for (String role : rolesArray) {
					rolePs.setString(1, empId);
					rolePs.setString(2, role);
					rolePs.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println("Error during input or insertion:");
			System.out.println("Error  :" + e.getMessage());
		}
	}
	public void updateEmployee(String id, String name, String dept, String dob, String address, String email) {
		String sql = "update employee set  name=?, dept=?, dob=?, address=?, email=? WHERE id=?";
		try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, dept);
			ps.setDate(3, Date.valueOf(dob));
			ps.setString(4, address);
			ps.setString(5, email);
			ps.setString(6, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error  :" + e.getMessage());
		}
	}
	public void deleteEmployee(String id) {
		String sql = "delete from users where id=?";
		try (Connection con =  Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error  :" + e.getMessage());
		}
	}
	public void viewEmployee() {
		String sql = "select * from users";
		try (Connection con = Db.getConnection();Statement st = con.createStatement();ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				System.out.println(rs.getString("id") + " | " + rs.getString("name") + " | " + rs.getString("dept"));
			}
		} catch (Exception e) {
			System.out.println("Error  :" + e.getMessage());
		}
	}
	public void viewEmployee_by_id(String id) {
		String sql = "select * from users where id=?";
		try (Connection con = Db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
	    	ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("Name  : " + rs.getString("name"));
				System.out.println("Dept  : " + rs.getString("dept"));
				System.out.println("Email : " + rs.getString("email"));
			}
		} catch (Exception e) {
			System.out.println("Error  :" + e.getMessage());
		}
	}
	public void changePassword(String id, String oldPass, String newPass) {
	    String checkSql = "select password from users where id=?";
	    String updateSql = "update users set password=? where id=?";
	    try (Connection con = Db.getConnection();
	         PreparedStatement checkPs = con.prepareStatement(checkSql); PreparedStatement updatePs = con.prepareStatement(updateSql)) {
	        checkPs.setString(1, id);
	        ResultSet rs = checkPs.executeQuery();
	        if (rs.next()) {
	            String dbPassword = rs.getString("password");
	            if (!dbPassword.equals(oldPass)) {
	                System.out.println("old password incorrect");
	                return;
	            }
	            updatePs.setString(1, newPass);
	            updatePs.setString(2, id);
	            updatePs.executeUpdate();
	            System.out.println("password changed");
	        }
	    } catch (Exception e) {
	    	System.out.println("Error  :" + e.getMessage());
	    }
	}
	public void resetPassword(String id, String password) {
	    String sql = "update users set password=? where id=?";
	    try (Connection con = Db.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, password);
	        ps.setString(2, id);
	        ps.executeUpdate();
	        System.out.println("password reset");
	    } catch (Exception e) {
	    	System.out.println("Error  :" + e.getMessage());
	    }
	}	
	public void grantRole(String id, String role) {
	    String sql = "insert into user_role (emp_id, role) values(?, ?)";
	    try (Connection con = Db.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, id);
	        ps.setString(2, role);
	        ps.executeUpdate();
	        System.out.println("Role granted");
	    } catch (Exception e) {
	    	System.out.println("Error  :" + e.getMessage());
	    }
	}
	public void revokeRole(String id, String role) {
	    String sql = "delete from user_roles where emp_id=? and role=?";
	    try (Connection con = Db.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, id);
	        ps.setString(2, role);
	        ps.executeUpdate();
	        System.out.println("Role revoked ");
	    } catch (Exception e) {
	    	System.out.println("Error  :" + e.getMessage());
	    }
	}
	public Employee getEmployeeById(String id) {
	    String empSql = "select * from users where id=?";
	    String roleSql = "select role from user_roles where emp_id=?";
	    Employee emp = null;
	    try (Connection con = Db.getConnection();
	         PreparedStatement empPs = con.prepareStatement(empSql);
	         PreparedStatement rolePs = con.prepareStatement(roleSql)) {
	        empPs.setString(1, id);
	        ResultSet rs = empPs.executeQuery();
	        if (rs.next()) {
	            emp = new Employee();
	            emp.setId(rs.getString("id"));
	            emp.setName(rs.getString("name"));
	            emp.setDept(rs.getString("dept"));
	            emp.setDob(rs.getDate("dob").toString());
	            emp.setAddress(rs.getString("address"));
	            emp.setEmail(rs.getString("email"));
	            rolePs.setString(1, id);
	            ResultSet roleRs = rolePs.executeQuery();
	            List<String> roles = new ArrayList<>();
	            while (roleRs.next()) {
	                roles.add(roleRs.getString("role"));
	            }
	           
	        }
	    } catch (Exception e) {
	    	System.out.println("Error  :" + e.getMessage());
	    }
	    return emp;
	}
}

	
  
