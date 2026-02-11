package com.employee.services;

import com.employee.controller.Menu;
import com.employee.dao.EmployeeDao;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeService {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	private final EmployeeUtil util = new EmployeeUtil();

	public void insert(EmployeeDao empdao, Employee employee) {
		logger.info("add employee request recieved for name {}", employee.getName());
		if (!util.validateName(employee.getName())) {
			logger.warn("validation failed: invalid name for name {}", employee.getName());
			throw new ValidationException("Invalid name");
		}
		if (!util.validateDept(employee.getDept())) {
			logger.warn("validation failed: invalid dept for name {}", employee.getName());
			throw new ValidationException("Invalid department");
		}
		if (!util.validateDob(employee.getDob())) {
			logger.warn("validation failed: invalid Dob for name {}", employee.getName());
			throw new ValidationException("Invalid DOB");
		}
		if (!util.validateAddress(employee.getAddress())) {
			logger.warn("validation failed: invalid address for name {}", employee.getAddress());
			throw new ValidationException("Invalid address");
		}
		if (!util.validateEmail(employee.getEmail())) {
			logger.warn("validation failed: invalid email for name {}", employee.getName());
			throw new ValidationException("Invalid email");
		}
		if (!util.validateRole(employee.getRoles())) {
			logger.warn("validation failed: invalid role for name {}", employee.getName());
			throw new ValidationException("Invalid roles");
		}
		if (!util.validatePassword(employee.getPassword())) {
			logger.warn("validation failed: invalid password for name {}", employee.getName());
			throw new ValidationException("Invalid password");
		}
		try {
			empdao.addEmployee(employee);
			logger.info("employee added succesfully with name {}", employee.getName());
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error during adding employee with name {}", employee.getName(), e);
			throw new ServiceException("Unable to add employee at this time " + e.getMessage());
		}
	}

	public boolean grantRole(String empId, String roleStr, EmployeeDao dao) {
		empId = empId.trim().toUpperCase();
		logger.info("assign role {} request recieved for id {} ", roleStr, empId);
		if (!util.validateId(empId)) {
			logger.warn("validation failed: invalid Id {}",empId);
			throw new ValidationException("Invalid employee ID");
		}
		if (!util.validateSingleRole(roleStr)) {
			logger.warn("validation failed: invalid role ");
			throw new ValidationException("Invalid role: " + roleStr);
		}
		Roles role = Roles.valueOf(roleStr.trim().toUpperCase());
		try {
			boolean result = dao.grantRole(empId, role);
			if (!result) {
				logger.warn("duplicate role {} is assigned for id {} ", role, empId);
				throw new ValidationException("role already exists");
			}
			logger.info("assign role {} succesfully to id {} ", role, empId);
		} catch (EmployeeDoesNotExists | DataAccessException e) {
			throw new ServiceException("Unable to grant role for " + empId + e.getMessage());
		}
		return true;
	}

	public boolean revokeRole(String empId, String roleStr, EmployeeDao dao) {
		empId = empId.trim().toUpperCase();
		logger.info("revoke role {} request recieved for id {} ", roleStr, empId);
		if (!util.validateId(empId)) {
			logger.warn("validation failed: invalid Id {}",empId);
			throw new ValidationException("Invalid employee ID");
		}
		if (!util.validateSingleRole(roleStr)) {
			logger.warn("validation failed: invalid role");
			throw new ValidationException("Invalid role");
		}
		Roles role = Roles.valueOf(roleStr.trim().toUpperCase());
		try {
			boolean result = dao.revokeRole(empId, role);
			if (!result) {
				logger.warn("role {} doesn't assigned for id {} ", role, empId);
				throw new ValidationException("role doesnt assigned");
			}
			logger.info("revoke role {} succesfully to id {} ", role, empId);
		} catch (EmployeeDoesNotExists | DataAccessException e) {
			throw new ServiceException("Unable to revoke role for " + empId + e.getMessage());
		}
		return true;
	}

	public boolean delete(EmployeeDao dao, String id) {
		id = id.toUpperCase();
		logger.info("delete employee request recieved for id {}", id);
		if (!util.validateId(id)) {
			logger.warn("validation failed: invalid Id {}",id);
			throw new ValidationException("Invalid employee ID");
		}
		try {
			  return dao.deleteEmployee(id);
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			throw new ServiceException("unable to delete employee"+ e.getMessage());
		}
		
	}

	public List<Employee> getAllEmployees(EmployeeDao dao) {
		logger.info("fetch all employees request recieved");
		List<Employee> employees = new ArrayList<>();
		try {
			employees = dao.viewAllEmployee();
			logger.info("fetch all the employees successfully");
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error while fetch employees", e);
			throw new ServiceException("Unable to fetch employees details: " + e.getMessage());
		}
		return employees;
	}

	public Employee getEmployeeById(EmployeeDao dao, String id) {
		Employee employee = null;
		logger.info("fetch the employee request recieved for id {} ", id);
		if (!Menu.currentUser.getRoles().contains(Roles.ADMIN)
				&& !Menu.currentUser.getRoles().contains(Roles.MANAGER)) {
			logger.warn("Authorized error: user {} tried to access employee with id {} ", id);
			throw new ServiceException("Access denied");
		}
		if (!util.validateId(id)) {
			logger.warn("validation failed: invalid Id {}",id);
			throw new ValidationException("Invalid employee ID format");
		}
		try {
			employee = dao.viewEmployeeById(id);
			logger.info("fetch the employee successfully with id {}", id);
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error while fetch the employee with id {} ", id, e);
			throw new ServiceException("Unable to fetch employee details: " + e.getMessage());
		}
		return employee;
	}

	public boolean update(Employee employee, Roles role, EmployeeDao dao) {
		logger.info("update employee request recieved for id {} ", employee.getId());
		if (!role.equals(Roles.USER)) {
			if (!util.validateName(employee.getName())) {
				logger.warn("validation failed: invalid Name for {}", employee.getId());
				throw new ValidationException("Invalid name");
			}
			if (!util.validateDept(employee.getDept())) {
				logger.warn("validation failed: invalid Dept for {}", employee.getId());
				throw new ValidationException("Invalid dept");
			}
		}
		if (!util.validateDob(employee.getDob())) {
			logger.warn("validation failed: invalid Dob for {}", employee.getId());
			throw new ValidationException("Invalid dob");
		}
		if (!util.validateAddress(employee.getAddress())) {
			logger.warn("validation failed: invalid Address for {}", employee.getId());
			throw new ValidationException("Invalid address");
		}
		if (!util.validateEmail(employee.getEmail())) {
			logger.warn("validation failed: invalid email for {}", employee.getId());
			throw new ValidationException("Invalid email");
		}
		try {
			return dao.updateEmployee(employee, role);
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error while update the employee for id {} ", employee.getId(), e);
			throw new ServiceException("unable to update employee deails" + e.getMessage());
		}
	}
}
