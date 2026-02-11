package com.employee.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ValidationException;
import com.employee.model.Employee;
import com.employee.util.Roles;

@ExtendWith(MockitoExtension.class)
public class AddEmployeeTest {
	@Mock
	private EmployeeDao employeeDao;
	@InjectMocks
	private EmployeeService addEmployee;
	@Test
	void addEmployee_validEmployee_shouldCallDao() {
	    List<Roles> roles = List.of(Roles.ADMIN, Roles.USER);
		Employee emp = new Employee("Kumar", "DEVELOPER", "2002-03-03", "Kerala", "rohith@gmail.com",
				roles, "Old@1234");
		addEmployee.insert(employeeDao, emp);
	}
	@Test
	void addEmployee_invalidEmail_shouldThrowException() {
		Employee emp = new Employee("Kumar", "DEVELOPER", "2003-03-04", "Kerala", "rohithgmail.com",
				List.of(Roles.ADMIN), "hashedPwd");
		ValidationException ex = assertThrows(ValidationException.class, () -> addEmployee.insert(employeeDao, emp));
		assertEquals("Invalid email", ex.getMessage());
	}
	@Test
	void addEmployee_invalidRoles_shouldThrowException() {
		Employee emp = new Employee("Kumar", "DEVELOPER", "2004-01-02", "Kerala", "rohith@gmail.com",
				List.of(), "hashedPwd");
		ValidationException ex = assertThrows(ValidationException.class, () -> addEmployee.insert(employeeDao, emp));
		assertEquals("Invalid roles", ex.getMessage());
	}
	@Test
	void addEmployee_invalidDob_shouldThrowException() {
		Employee emp = new Employee("Kumar", "DEVELOPER", "04-03-2000", "Kerala", "rohith@gmail.com",
				List.of(Roles.ADMIN), "hashedPwd");
		ValidationException ex = assertThrows(ValidationException.class, () -> addEmployee.insert(employeeDao, emp));
		assertEquals("Invalid DOB", ex.getMessage());
	}
	@Test
	void addEmployee_emptyName_shouldThrowException() {
		Employee emp = new Employee("", "DEVELOPER", "2001-06-04", "Kerala", "rohith@gmail.com", List.of(Roles.ADMIN),
				"hashedPwd");
		ValidationException ex = assertThrows(ValidationException.class, () -> addEmployee.insert(employeeDao, emp));
		assertEquals("Invalid name", ex.getMessage());
	}
}
