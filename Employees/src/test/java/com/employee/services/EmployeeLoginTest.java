package com.employee.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ValidationException;
import com.employee.model.LoginResult;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;

@ExtendWith(MockitoExtension.class)
public class EmployeeLoginTest {
    @Mock
    private EmployeeDao employeeDao;
    @InjectMocks
    private LoginService employeeLoginService;
    private final String validEmployeeId = "TEK-11";
    private final String validPassword = "pass";
    private final String invalidEmployeeId = "tek";
    private final EmployeeUtil util = new EmployeeUtil();
    private LoginResult validLoginResult;
    @BeforeEach
    void setUp() {
        validLoginResult = new LoginResult(true,validEmployeeId,List.of(Roles.USER));
    }
    @Test
    void loginValidEmployee_shouldReturnLoginResult() {
        when(employeeDao.validateUser(validEmployeeId, "Old@1234")).thenReturn(validLoginResult);
        LoginResult result = employeeLoginService.validate(validEmployeeId, "Old@1234",employeeDao);
        assertEquals(validEmployeeId, result.getEmpId());
        verify(employeeDao).validateUser(validEmployeeId, "Old@1234");
    }
    @Test
    void loginInvalidCredentials_shouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeLoginService.validate(validEmployeeId, "",employeeDao);
        });
        assertEquals("Invalid username or password", exception.getMessage());
        verify(employeeDao,never()).validateUser(validEmployeeId, util.hash(""));
    }
    @Test
    void loginNonExistentEmployee_shouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeLoginService.validate(invalidEmployeeId, validPassword,employeeDao);
        });
        assertEquals("Invalid username or password", exception.getMessage());
        verify(employeeDao,never()).validateUser(invalidEmployeeId, util.hash(validPassword));
    }
    @Test
    void loginEmptyUsername_shouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeLoginService.validate("", validPassword,employeeDao);
        });
        assertEquals("Invalid username or password", exception.getMessage());
    }
    @Test
    void loginEmptyPassword_shouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeLoginService.validate(validEmployeeId, "",employeeDao);
        });
        assertEquals("Invalid username or password", exception.getMessage());
    }
}
