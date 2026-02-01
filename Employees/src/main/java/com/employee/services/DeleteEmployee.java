package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ValidationException;
import com.employee.util.EmployeeUtil;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.exceptions.DataAccessException;

public class DeleteEmployee {
	private final EmployeeUtil util = new EmployeeUtil();
	public boolean delete(EmployeeDao dao,String id) {
		id = id.toUpperCase();
		if (!util.validateId(id)) {
            throw new ValidationException("Invalid employee ID");
        }
		try {
			boolean result = dao.deleteEmployee(id);
			if(!result) {
				throw new EmployeeDoesNotExists("Employee not found");
			}
			return true;
		}catch(DataAccessException e) {
		   throw new ServiceException("unable to delete employee for "+id+e.getMessage(), e);
		}
	}
}
 