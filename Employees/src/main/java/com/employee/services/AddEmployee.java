package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.ServiceException;
public class AddEmployee {
  private final EmployeeUtil util = new EmployeeUtil();
  GetEmployee getEmployee = new GetEmployee();
 public void insert(EmployeeDao empdao,Employee employee) { 
	 if (!util.validateName(employee.getName()))
		   throw new ValidationException("Invalid name");
     if (!util.validateDept(employee.getDept()))
		   throw new ValidationException("Invalid department");
     if (!util.validateDob(employee.getDob()))
		   throw new ValidationException("Invalid DOB");
     if (!util.validateAddress(employee.getAddress()))
           throw new ValidationException("Invalid address");
     if (!util.validateEmail(employee.getEmail()))
		    throw new ValidationException("Invalid email");
     if(!util.validateRole(employee.getRoles()))
    	    throw new ValidationException("Invalid roles");
     if (!util.validatePassword(employee.getPassword()))
		    throw new ValidationException("Invalid password");
    try {
     empdao.addEmployee(employee);
	 getEmployee.getAllEmployees(empdao);
   }catch(DataAccessException |EmployeeDoesNotExists e) {  
	   throw new ServiceException("Unable to add employee at this time "+e.getMessage(), e);
   }
}
}

