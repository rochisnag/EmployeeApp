package com.employee.services;

import com.employee.dao.EmployeeDao;
import com.employee.exceptions.ServiceException;
import com.employee.model.LoginResult;
import com.employee.exceptions.ValidationException;
import com.employee.exceptions.DataAccessException;
import com.employee.exceptions.EmployeeDoesNotExists;
import com.employee.util.EmployeeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {
	private final EmployeeUtil util = new EmployeeUtil();
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	public LoginResult validate(String id, String password, EmployeeDao dao) {
		logger.info("login request recieved for id {} ", id);
		if (!util.validateId(id)) {
			logger.warn("validation failed: invalid id for {}", id);
			throw new ValidationException("Invalid username or password");
		}
		if (!util.validatePassword(password)) {
			logger.warn("validation failed: invalid password for {}", id);
			throw new ValidationException("Invalid username or password");
		}
		try {
			LoginResult login = dao.validateUser(id, password);
			return login;
		} catch (DataAccessException  | EmployeeDoesNotExists e) {
			logger.error("Database error while login {} ", id);
			throw new ServiceException(" unable to login  " + e.getMessage());
		}
	}

	private String getHashedDefaultPassword() {
		String defaultPassword = System.getenv("DefaultPassword");
		if (defaultPassword == null || defaultPassword.isBlank()) {
			throw new ServiceException("DEFAULT_PASSWORD environment variable is not set");
		}
		if (!util.validatePassword(defaultPassword)) {
			throw new ValidationException("Default password format is invalid");
		}
		return EmployeeUtil.hash(defaultPassword);
	}

	public boolean changePassword(EmployeeDao dao, String id, String oldPassword, String newPassword) {
		logger.info("change password request recieved for id {} ", id);
		if (!util.validateId(id)) {
			logger.warn("validation failed: invalid id for id {}", id);
			throw new ValidationException("Invalid ID format");
		}
		if (!util.validatePassword(newPassword)) {
			logger.warn("validation failed: invalid password for id {}", id);
			throw new ValidationException("Invalid New password format");
		}
		try {
			boolean result = dao.changePassword(id.toUpperCase(), oldPassword, newPassword);
			if (!result) {
				logger.warn("employee not found with id {} ", id);
				throw new ServiceException("Failed to change password");
			}
			logger.info("change password successfully for id {} ", id);
			return true;
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error while change the password for id {} ", id, e);
			throw new ServiceException("Unable to change password for " + id + e.getMessage());
		}
	}

	public boolean resetPassword(EmployeeDao dao, String id) {
		logger.info("reset password request recieved for id {} ", id);
		if (!util.validateId(id)) {
			logger.warn("validation failed: invalid id for {}", id);
			throw new ValidationException("Invalid  ID format");
		}
		try {
			String hashedPassword = getHashedDefaultPassword();
			boolean result = dao.resetPassword(id.toUpperCase(), hashedPassword);
			if (!result) {
				logger.warn("employee not found with id {} ", id);
				throw new ServiceException("failed to reset password");
			}
			logger.info("reset password successfully for id {} ", id);
			return true;
		} catch (DataAccessException | EmployeeDoesNotExists e) {
			logger.error("Database error while reset the password for id {} ", id, e);
			throw new ServiceException("Unable to reset password for " + id + e.getMessage());
		}
	}
}
