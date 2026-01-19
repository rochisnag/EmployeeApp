package com.employee.util;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
public class RolePermission {
 private static  Map<Roles,Set<Operations>> map=new HashMap<>(); 
	public RolePermission() {
		map.put(Roles.ADMIN,EnumSet.of(
				Operations.ADD,
				Operations.UPDATE,
				Operations.FETCH,
				Operations.FETCH_EMPLOYEE_BY_ID,
				Operations.DELETE,
				Operations.RESETPASSWORD,
				Operations.GRANT,
				Operations.REVOKE,
				Operations.EXIT
				));
		map.put(Roles.MANAGER, EnumSet.of(
				Operations.FETCH,
				Operations.FETCH_EMPLOYEE_BY_ID,
				Operations.UPDATE,
				Operations.EXIT
				));
		map.put(Roles.USER, EnumSet.of(
				Operations.FETCH_EMPLOYEE_BY_ID,
				Operations.UPDATE,
				Operations.CHANGEPASSWORD,
				Operations.EXIT
				));
	}
	public boolean hasAccess(List<Roles> roles,Operations operation) {
		for(Roles role:roles) {
			   if (map.getOrDefault(role, Collections.emptySet()).contains(operation)) {
				return true;
			}
		}
		return false;
	}
}