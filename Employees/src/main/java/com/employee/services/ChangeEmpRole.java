package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;

public class ChangeEmpRole {
    private final EmployeeUtil util = new EmployeeUtil();
    public boolean grantRole(String empId, String roleStr, EmployeeDao dao) {
        empId = empId == null ? null : empId.trim().toUpperCase();
        if (!util.validateId(empId)) {
            throw new ValidationException("Invalid employee ID");
        }
        if (!util.validateSingleRole(roleStr)) {
            throw new ValidationException("Invalid role: " + roleStr);
        }
        Roles role = Roles.valueOf(roleStr.trim().toUpperCase());
        try {
          boolean result =  dao.grantRole(empId, role);
          if(!result) {
        	  throw new ServiceException("Failed to grant role");
          }
          return true;
        } catch (EmployeeDoesNotExists | DataAccessException e) {
            throw new ServiceException("Unable to grant role for " + empId+e.getMessage(), e);
        }
    }
    public boolean revokeRole(String empId, String roleStr, EmployeeDao dao) {
    	  empId = empId.trim().toUpperCase();
        if (!util.validateId(empId)) {
            throw new ValidationException("Invalid employee ID");
        }
        if(!util.validateSingleRole(roleStr)) {
        	throw new ValidationException("Invalid role");
        }
        Roles role = Roles.valueOf(roleStr.trim().toUpperCase()); 
        try {
           boolean result = dao.revokeRole(empId, role);
           if(!result) {
        	   throw new ServiceException("Failed to grant role"); 
           }
           return true;
        } catch (EmployeeDoesNotExists| DataAccessException e) {
            throw new ServiceException("Unable to revoke role for "+ empId+e.getMessage(), e);
        } 
    }
}
