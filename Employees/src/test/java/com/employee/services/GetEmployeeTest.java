package com.employee.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.controller.Menu;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.model.Employee;
import com.employee.model.LoginResult;
import com.employee.util.Roles;

@ExtendWith(MockitoExtension.class)
class GetEmployeeTest {

	@Mock
	private EmployeeDao employeeDao;

	private EmployeeService getEmployee;

	@BeforeEach
	void setUp() {
		getEmployee = new EmployeeService();
	}

	@Test
	void getAllEmployees_shouldReturnEmployeeList() {
		List<Employee> employees = Arrays.asList(
				new Employee("TEK-1", "John", "IT", "1999-01-01", "Chennai", "john@gmail.com"),
				new Employee("TEK-2", "Jane", "HR", "1998-02-02", "Bangalore", "jane@gmail.com"));
		when(employeeDao.viewAllEmployee()).thenReturn(employees);
		List<Employee> result = getEmployee.getAllEmployees(employeeDao);
		assertEquals(2, result.size());
	}

	@Test
	void getAllEmployees_daoThrowsException_shouldThrowServiceException() {
		when(employeeDao.viewAllEmployee()).thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class, () -> getEmployee.getAllEmployees(employeeDao));
	}

	@Test
	void getEmployeeById_notAdminOrManager_shouldThrowServiceException() {
		Menu.currentUser = new LoginResult(true, "TEK-1", Collections.singletonList(Roles.USER));

		assertThrows(ServiceException.class, () -> getEmployee.getEmployeeById(employeeDao, "TEK-2"));
	}

	@Test
	void getEmployeeById_invalidId_shouldThrowValidationException() {
		Menu.currentUser = new LoginResult(true, "TEK-1", Collections.singletonList(Roles.ADMIN));
		assertThrows(ValidationException.class, () -> getEmployee.getEmployeeById(employeeDao, "bhd"));
	}

	@Test
	void getEmployeeById_employeeNotFound_shouldThrowEmployeeDoesNotExists() {
		Menu.currentUser = new LoginResult(true, "TEK-99", Collections.singletonList(Roles.ADMIN));
		doThrow(new EmployeeDoesNotExists("Employee does not exist with ID:")).when(employeeDao).viewEmployeeById(anyString());
		assertThrows(ServiceException.class, () -> getEmployee.getEmployeeById(employeeDao, "TEK-99"));
	}

	@Test
	void getEmployeeById_daoThrowsException_shouldThrowServiceException() {
		Menu.currentUser = new LoginResult(true, "ADMIN", Collections.singletonList(Roles.ADMIN));
		when(employeeDao.viewEmployeeById("TEK-1")).thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class, () -> getEmployee.getEmployeeById(employeeDao, "TEK-1"));
	}

	@Test
	void getEmployeeById_adminValidRequest_shouldReturnEmployee() {
		Menu.currentUser = new LoginResult(true, "ADMIN", Collections.singletonList(Roles.ADMIN));
		Employee emp = new Employee("TEK-1", "John", "IT", "1999-01-01", "Chennai", "john@gmail.com");
		when(employeeDao.viewEmployeeById("TEK-1")).thenReturn(emp);
		Employee result = getEmployee.getEmployeeById(employeeDao, "TEK-1");
		assertNotNull(result);
		assertEquals("TEK-1", result.getId());
	}
}
