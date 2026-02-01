package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.EmployeeDoesNotExists;  
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import com.employee.controller.MenuController;
import com.employee.model.Employee;
import java.util.List;

public class GetEmployee {
    private final EmployeeUtil util = new EmployeeUtil();
    public List<Employee> getAllEmployees(EmployeeDao dao) {
        try {
          List<Employee> employees =   dao.viewAllEmployee();
          return employees;
        } catch (DataAccessException | EmployeeDoesNotExists e) {
        	throw new ServiceException("Unable to fetch employees details: " + e.getMessage(), e);
        }
    }	
    public Employee getEmployeeById(EmployeeDao dao,String id){
            if (!MenuController.currentUser.getRoles().contains(Roles.ADMIN)
                    && !MenuController.currentUser.getRoles().contains(Roles.MANAGER)) {
                throw new ServiceException("Access denied");
            }
            if (!util.validateId(id)) {
                throw new ValidationException("Invalid employee ID format");
            }
           try {
        	   Employee employee = dao.viewEmployeeById(id);
        	   if(employee==null) {
        		   throw new EmployeeDoesNotExists("employee not found");
        	   }
                 return employee;
        } catch (DataAccessException e) {
        	throw new ServiceException("Unable to fetch employee details: " + e.getMessage());
        }
    }
}
