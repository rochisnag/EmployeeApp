package com.employee.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordOperationsTest {
	@Mock
	private EmployeeDao employeeDao;
	@Mock
	private LoginService passwordOperations;

	@BeforeEach
	void setUp() {
		passwordOperations = new LoginService();
	}

	@Test
	void changePassword_validInputs_shouldSucceed() {
		when(employeeDao.changePassword("TEK-1", "Old@1234", "New@1234")).thenReturn(true);
		assertDoesNotThrow(() -> passwordOperations.changePassword(employeeDao, "TEK-1", "Old@1234", "New@1234"));

	}

	@Test
	void changePassword_invalidId_shouldThrowValidationException() {
		assertThrows(ValidationException.class,
				() -> passwordOperations.changePassword(employeeDao, "bbd", "old", "New@123"));
	}

	@Test
	void changePassword_invalidNewPassword_shouldThrowValidationException() {
		assertThrows(ValidationException.class,
				() -> passwordOperations.changePassword(employeeDao, "TEK-1", "old@123", "short"));
	}

	@Test
	void changePassword_daoThrowsException_shouldThrowServiceException() {
		when(employeeDao.changePassword(anyString(), anyString(), anyString()))
				.thenThrow(new DataAccessException("DB error"));
		assertThrows(ServiceException.class,
				() -> passwordOperations.changePassword(employeeDao, "TEK-1", "Old@1234", "New@1234"));
	}

	@Test
	void resetPassword_invalidId_shouldThrowValidationException() {
		assertThrows(ValidationException.class, () -> passwordOperations.resetPassword(employeeDao, "ghc"));
	}

}
