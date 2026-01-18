package com.employee.model;
import java.util.List;
import com.employee.util.Roles;

public class LoginResult {
    private boolean success;
    private String empId;
    private   List<Roles> roles;
   public LoginResult(boolean success, String empId, List<Roles> roles) {
        this.success = success;
        this.empId = empId;
        this.roles = roles;
    }
    public boolean getSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public List<Roles> getRoles() {
        return roles;
    }
    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
    public boolean hasRole(Roles role) {
        return roles != null && roles.contains(role);
    }
}
