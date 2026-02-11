package com.employee.model;
import java.util.List;
import com.employee.util.Roles;

public class Session {
	private final LoginResult currentUser;

	public Session(LoginResult currentUser) {
		this.currentUser = currentUser;
	}

	public String getId() {
		return currentUser.getEmpId();
	}

	public List<Roles> getRoles() {
		return currentUser.getRoles();
	}

	public LoginResult getUser() {
		return currentUser;
	}

}
