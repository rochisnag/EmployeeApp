package com.employee.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.dao.EmployeeDao;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.model.Employee;

@ExtendWith(MockitoExtension.class)
public class DeleteEmployeeTest {
    @Mock
    private EmployeeDao employeeDao;
    @InjectMocks
    private EmployeeService deleteEmployee;
    private Employee validEmployee;
    @BeforeEach
    public void setUp() {
        validEmployee = new Employee("TEK-1", "SAI", "DEV", "1990-01-01", "hyderabad", "sai123@gmail.com");
    }
    @Test
    void deleteEmployee_validEmployeeId_shouldReturnTrue() {
        when(employeeDao.deleteEmployee(validEmployee.getId())).thenReturn(true);
        boolean result = deleteEmployee.delete(employeeDao, validEmployee.getId());
        assertEquals(true, result);
        verify(employeeDao).deleteEmployee(validEmployee.getId());
    }
    @Test
    void deleteEmployee_nonExistentEmployeeId_shouldThrowEmployeeDoesNotExistException() {
//        when(employeeDao.deleteEmployee("TEK-13")).thenReturn(false);
    	doThrow(new EmployeeDoesNotExists("Employee does not exist with ID:")).when(employeeDao).deleteEmployee("TEK-13");
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            deleteEmployee.delete(employeeDao, "TEK-13");
        });
        assertEquals("unable to delete employeeEmployee does not exist with ID:", exception.getMessage());
    }
    @Test
    void deleteEmployee_invalidEmployeeIdFormat_shouldThrowValidationException() {
        ValidationException e = assertThrows(ValidationException.class, () -> {
            deleteEmployee.delete(employeeDao, "tek1");
        });
        assertEquals("Invalid employee ID", e.getMessage());
    }
    @Test
    void deleteEmployee_emptyEmployeeId_shouldThrowValidationException() {
        ValidationException e = assertThrows(ValidationException.class, () -> {
            deleteEmployee.delete(employeeDao, "");
        });
        assertEquals("Invalid employee ID", e.getMessage());
    }
}
