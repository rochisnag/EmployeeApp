package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ServiceException;
import com.employee.model.LoginResult;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.LoginFailedException;
import com.employee.util.EmployeeUtil;
public class EmployeeLogin {
	EmployeeUtil util = new EmployeeUtil();
    public LoginResult validate(String id, String password, EmployeeDao dao){
        if (!util.validateId(id)){
            throw new  ValidationException("Invalid username or password");
        }
        if (!util.validatePassword(password)) {
            throw new ValidationException("Invalid username or password");
        }
        try {
        	  LoginResult login = dao.validateUser(id, password);
        	  return login;
        }catch(LoginFailedException  e) {
        	throw new ServiceException(" unable to login"+e.getMessage(), e);
        }
    } 
}
