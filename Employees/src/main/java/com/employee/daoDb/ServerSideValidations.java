package com.employee.daoDb;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import com.employee.util.Db;

public class ServerSideValidations {
public String IdAutogenerate() {
	String sql = "select id from users order by id desc limit 1 ";
	try (Connection con = Db.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql)){
		if(rs.next()) {
			String lastId = rs.getString("id");
			int num = Integer.parseInt(lastId.split("-")[1]);
			return "TEK-"+(num+1);
		}
	}
   catch(Exception e) {
	   System.out.println("Error :"+e.getMessage());
}
	return "TEK-1";
}
}