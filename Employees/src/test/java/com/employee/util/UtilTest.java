package com.employee.util;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class UtilTest{
    private EmployeeUtil util;
    @BeforeEach
    void setup() {
        util = new EmployeeUtil();
    }
    @Test
    void testHashAndVerifyPasswordPositive() {
        String password = "Default123@";
        String hashedPassword = util.hash(password);
        assertNotNull(hashedPassword);
        assertTrue(BCrypt.checkpw(password, hashedPassword));
        assertTrue(util.verify(password, hashedPassword));
    }
    @Test
    void testVerifyFailsForWrongPasswordNegative() {
        String password = "Default123";
        String wrongPassword = "Pass123";
        String hashedPassword = util.hash(password);
        assertFalse(util.verify(wrongPassword, hashedPassword));
    }
    @Test
    void testVerifyFailsForNullOrEmptyPasswordNegative() {
        String hashedPassword = util.hash("Pass123!");
        assertThrows(NullPointerException.class, () -> util.verify(null, hashedPassword));
        assertFalse(util.verify("", hashedPassword));
    }
    @Test
    void testRandomPasswordGenerationPositive() {
        String randomPassword = util.generateRandomPassword();
        assertNotNull(randomPassword);
        assertEquals(8, randomPassword.length());
    }
    @Test
    void testRandomPasswordUniquenessPositive() {
        String pw1 = util.generateRandomPassword();
        String pw2 = util.generateRandomPassword();
        assertNotEquals(pw1, pw2);
    }
    @Test
    void testRandomPasswordNegative() {
        for (int i = 0; i < 10; i++) {
            String pw = util.generateRandomPassword();
            assertNotNull(pw);
            assertFalse(pw.isEmpty());
        }
    }
}
