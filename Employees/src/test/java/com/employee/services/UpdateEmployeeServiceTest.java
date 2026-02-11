package com.employee.services;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.model.Employee;
import com.employee.util.Roles;

@ExtendWith(MockitoExtension.class)
class UpdateEmployeeServiceTest {

	@Mock
	private EmployeeDao employeeDao;

	@InjectMocks
	private EmployeeService updateEmployee;
	private Employee validEmployee;

	@BeforeEach
	void setUp() {
		validEmployee = new Employee();
		validEmployee.setId("EMP1");
		validEmployee.setName("Rohith");
		validEmployee.setDept("IT");
		validEmployee.setDob("1999-01-01");
		validEmployee.setAddress("Bangalore");
		validEmployee.setEmail("rohith@gmail.com");
	}

	@Test
	void update_adminRole_validEmployee_shouldReturnTrue() {
		when(employeeDao.updateEmployee(validEmployee, Roles.ADMIN)).thenReturn(true);
		boolean result = updateEmployee.update(validEmployee, Roles.ADMIN, employeeDao);
		assertTrue(result);
		verify(employeeDao).updateEmployee(validEmployee, Roles.ADMIN);
	}

	@Test
	void update_adminRole_invalidName_shouldThrowValidationException() {
		validEmployee.setName("");
		assertThrows(ValidationException.class, () -> updateEmployee.update(validEmployee, Roles.ADMIN, employeeDao));
	}

	@Test
	void update_adminRole_invalidDept_shouldThrowValidationException() {
		validEmployee.setDept("");
		assertThrows(ValidationException.class, () -> updateEmployee.update(validEmployee, Roles.ADMIN, employeeDao));
	}

	@Test
	void update_invalidDob_shouldThrowValidationException() {
		validEmployee.setDob("01-01-1999");
		assertThrows(ValidationException.class, () -> updateEmployee.update(validEmployee, Roles.USER, employeeDao));
	}

	@Test
	void update_invalidAddress_shouldThrowValidationException() {
		validEmployee.setAddress("");
		assertThrows(ValidationException.class, () -> updateEmployee.update(validEmployee, Roles.USER, employeeDao));
	}

	@Test
	void update_invalidEmail_shouldThrowValidationException() {
		validEmployee.setEmail("snjdgmail.com");
		assertThrows(ValidationException.class, () -> updateEmployee.update(validEmployee, Roles.USER, employeeDao));
	}

	@Test
	void update_daoThrowsDataAccessException_shouldThrowServiceException() {
		when(employeeDao.updateEmployee(validEmployee, Roles.ADMIN)).thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class, () -> updateEmployee.update(validEmployee, Roles.ADMIN, employeeDao));
	}

	@Test
	void update_employeeDoesNotExist_shouldThrowServiceException() {
		when(employeeDao.updateEmployee(validEmployee, Roles.ADMIN)).thenThrow(new EmployeeDoesNotExists("Employee not found"));
		assertThrows(ServiceException.class, () -> updateEmployee.update(validEmployee, Roles.ADMIN, employeeDao));
	}
}
