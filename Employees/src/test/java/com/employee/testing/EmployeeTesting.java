package com.employee.testing;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;

class EmployeeTesting {
    private EmployeeUtil validation;
    private Employee employee;
    @BeforeEach
    void setUp() {
        employee = new Employee();
        validation = new EmployeeUtil();
    }
    @Test
    void testValidateIdPositive() {
        assertTrue(validation.validateId("TEK-12345"));
        assertEquals("TEK-12345", employee.getId());
    }
    @Test
    void testValidateIdNegative() {
        assertFalse(validation.validateId("123-TEK"));
        assertFalse(validation.validateId(""));
        assertFalse(validation.validateId(null));
    }
    @Test
    void testValidateNamePositive() {
        assertTrue(validation.validateName("John Doe"));
        assertEquals("John Doe", employee.getName());
    }
    @Test
    void testValidateNameNegative() {
        assertFalse(validation.validateName(""));
        assertFalse(validation.validateName("   "));
        assertFalse(validation.validateName(null));
    }
    @Test
    void testValidateDeptPositive() {
        assertTrue(validation.validateDept("IT"));
        assertEquals("IT", employee.getDept());
    }
    @Test
    void testValidateDeptNegative() {
        assertFalse(validation.validateDept(""));
        assertFalse(validation.validateDept(null));
    }
    @Test
    void testValidateDobPositive() {
        String dob = "1995-5-15";
        assertTrue(validation.validateDob(dob));
        assertEquals(dob, employee.getDob());
    }
    @Test
    void testValidateDobNegativeInvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> validation.validateDob("2000-02-30")); 
        int currentYear = java.time.LocalDate.now().getYear();
        assertThrows(IllegalArgumentException.class, () -> validation.validateDob((currentYear - 10) + "-01-01")); 
        assertThrows(IllegalArgumentException.class, () -> validation.validateDob((currentYear - 120) + "-01-01")); 
        assertThrows(IllegalArgumentException.class, () -> validation.validateDob(null));
    }
    @Test
    void testValidateAddressPositive() {
        assertTrue(validation.validateAddress("123 Main"));
        assertEquals("123 Main", employee.getAddress()); 
    }
    @Test
    void testValidateAddressNegative() {
        assertFalse(validation.validateAddress(""));
        assertFalse(validation.validateAddress("   "));
        assertFalse(validation.validateAddress(null));
    }
    @Test
    void testValidateEmailPositive() {
        assertTrue(validation.validateEmail("test@tek.com"));
        assertEquals("test@tek.com", employee.getEmail());
    }
    @Test
    void testValidateEmailNegative() {
        assertFalse(validation.validateEmail("invalid-email"));
        assertFalse(validation.validateEmail("user@domain"));
        assertFalse(validation.validateEmail(""));
        assertFalse(validation.validateEmail(null));
    }
    @Test
    void testValidatePasswordPositive() {
        assertTrue(validation.validatePassword("Password123"));
        assertEquals("Password123", employee.getPassword());
    }
    @Test
    void testValidatePasswordNegative() {
        assertFalse(validation.validatePassword(""));
        assertFalse(validation.validatePassword("   "));
        assertFalse(validation.validatePassword(null));
    }
    @Test
    void testValidateRolePositive() {
        Roles role = validation.validateRole("admin");
        assertEquals(Roles.ADMIN, role);
        role = validation.validateRole("Manager");
        assertEquals(Roles.MANAGER, role);
    }
    @Test
    void testValidateRoleNegative() {
        assertThrows(IllegalArgumentException.class, () -> validation.validateRole(""));
        assertThrows(IllegalArgumentException.class, () -> validation.validateRole("   "));
        assertThrows(IllegalArgumentException.class, () -> validation.validateRole(null));
        assertThrows(IllegalArgumentException.class, () -> validation.validateRole("invalidRole"));
    }
}
