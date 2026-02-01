package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.util.EmployeeUtil;

public class PasswordOperations {
    private final EmployeeUtil util;
    public PasswordOperations(EmployeeUtil util) {
        this.util = util;
    }
    private String getHashedDefaultPassword() {
        String defaultPassword = System.getenv("DEFAULT_PASSWORD");
        if (defaultPassword == null || defaultPassword.isBlank()) {
            throw new ServiceException("DEFAULT_PASSWORD environment variable is not set");
        }
        if (!util.validatePassword(defaultPassword)) {
            throw new ValidationException("Default password format is invalid");
        }
        return util.hash(defaultPassword);
    }
    public boolean changePassword(EmployeeDao dao, String id, String oldPassword, String newPassword) {
        if (!util.validateId(id)) {
            throw new ValidationException("Invalid ID format");
        }
        if (!util.validatePassword(newPassword)) {
            throw new ValidationException("Invalid New password format");
        }
        try {
            boolean result = dao.changePassword(id.toUpperCase(), oldPassword, newPassword);
            if(!result) {
            	throw new ServiceException("Failed to change password");	
            } 
            return true;
        } catch(DataAccessException | EmployeeDoesNotExists e) {
        	throw new ServiceException("Unable to change password for "+id+e.getMessage(), e);
        }
    }
    public boolean resetPassword(EmployeeDao dao, String id) {
        if (!util.validateId(id)) {
            throw new ValidationException("Invalid  ID format");
        }
        try {
            String hashedPassword = getHashedDefaultPassword();
           boolean result =  dao.resetPassword(id.toUpperCase(), hashedPassword);
           if(!result) {
        	   throw new ServiceException("failed to reset password");
           }
           return true;
        } catch (DataAccessException |EmployeeDoesNotExists e) {
            throw new ServiceException("Unable to reset password for "+id+e.getMessage(), e);
        }
    }
}
