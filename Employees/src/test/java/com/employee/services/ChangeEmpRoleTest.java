package com.employee.services;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.employee.util.Roles;

@ExtendWith(MockitoExtension.class)
class ChangeEmpRoleTest {
	@Mock
	private EmployeeDao employeeDao;
	@InjectMocks
	private EmployeeService changeEmpRole;

	@Test
	void grantRole_validInputs_shouldReturnTrue() {
		when(employeeDao.grantRole("TEK-1", Roles.ADMIN)).thenReturn(true);
		boolean result = changeEmpRole.grantRole("TEK-1", "ADMIN", employeeDao);
		assertTrue(result);
		verify(employeeDao).grantRole("TEK-1", Roles.ADMIN);
	}

	@Test
	void grantRole_invalidEmployeeId_shouldThrowValidationException() {
		assertThrows(ValidationException.class, () -> changeEmpRole.grantRole(" ", "ADMIN", employeeDao));
	}

	@Test
	void grantRole_invalidRole_shouldThrowValidationException() {
		assertThrows(ValidationException.class, () -> changeEmpRole.grantRole("EMP1", "INVALID_ROLE", employeeDao));
	}

	@Test
	void grantRole_employeeDoesNotExist_shouldThrowServiceException() {
		when(employeeDao.grantRole("TEK-13", Roles.ADMIN)).thenThrow(new EmployeeDoesNotExists("Not found"));
		assertThrows(ServiceException.class, () -> changeEmpRole.grantRole("TEK-13", "ADMIN", employeeDao));
	}

	@Test
	void grantRole_dataAccessException_shouldThrowServiceException() {
		when(employeeDao.grantRole("TEK-1", Roles.ADMIN)).thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class, () -> changeEmpRole.grantRole("TEK-1", "ADMIN", employeeDao));
	}

	@Test
	void revokeRole_validInputs_shouldReturnTrue() {
		when(employeeDao.revokeRole("TEK-1", Roles.ADMIN)).thenReturn(true);
		boolean result = changeEmpRole.revokeRole("TEK-1", "ADMIN", employeeDao);
		assertTrue(result);
		verify(employeeDao).revokeRole("TEK-1", Roles.ADMIN);
	}

	@Test
	void revokeRole_invalidEmployeeId_shouldThrowValidationException() {
		assertThrows(ValidationException.class, () -> changeEmpRole.revokeRole(" ", "ADMIN", employeeDao));
	}

	@Test
	void revokeRole_invalidRole_shouldThrowValidationException() {
		assertThrows(ValidationException.class, () -> changeEmpRole.revokeRole("EMP1", "BAD_ROLE", employeeDao));
	}

	@Test
	void revokeRole_employeeDoesNotExist_shouldThrowServiceException() {
		when(employeeDao.revokeRole("TEK-1", Roles.ADMIN)).thenThrow(new EmployeeDoesNotExists("Not found"));
		assertThrows(ServiceException.class, () -> changeEmpRole.revokeRole("TEK-1", "ADMIN", employeeDao));
	}

	@Test
	void revokeRole_dataAccessException_shouldThrowServiceException() {
		when(employeeDao.revokeRole("TEK-1", Roles.ADMIN)).thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class, () -> changeEmpRole.revokeRole("TEK-1", "ADMIN", employeeDao));
	}
}
