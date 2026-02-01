package com.employee.services;
import com.employee.dao.EmployeeDao;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;
import com.employee.util.Roles;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.ServiceException;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
public class UpdateEmployee {
	private final EmployeeUtil util = new EmployeeUtil();
	private final GetEmployee getEmployee = new GetEmployee();
	public boolean update(Employee emp, Roles role, EmployeeDao dao) {
			if (!role.equals(Roles.USER)) {
				if (!util.validateName(emp.getName()))
					throw new ValidationException("Invalid name");
				if (!util.validateDept(emp.getDept()))
					throw new ValidationException("Invalid dept");
			}
			if (!util.validateDob(emp.getDob()))
				throw new ValidationException("Invalid dob");
			if (!util.validateAddress(emp.getAddress()))
				throw new ValidationException("Invalid address");
			if (!util.validateEmail(emp.getEmail()))
				throw new ValidationException("Invalid email");
			try {
	            boolean isUpdated = dao.updateEmployee(emp, role);
	            if (isUpdated) {
	                if (!role.equals(Roles.USER)) {
	                    getEmployee.getAllEmployees(dao);
	                } else {
	                    getEmployee.getEmployeeById(dao, emp.getId());
	                }
	            }
	            return isUpdated;     
		} catch(DataAccessException |EmployeeDoesNotExists e) {
			throw new ServiceException("unable to update employee deails"+e.getMessage(), e);
		}
	}
}
