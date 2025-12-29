package com.employee.dao;

import java.io.BufferedReader;
import com.employee.exceptions.EmployeeDoesNotExists;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.employee.dao.View;

public class Delete{
	
	public static void execute() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("ENTER YOUR ID");
		String did = sc.nextLine();
		
		boolean present=false;
		JSONParser parser=new JSONParser();
		
		try {
			File file=new File("output.json");
			Object obj=parser.parse(new BufferedReader(new FileReader(file)));
			JSONArray employees=(JSONArray)obj;
			for(int i=0;i<employees.size();i++){
				JSONObject employee=(JSONObject) employees.get(i);
				String id = (String)employee.get("id");
				if(did.equals(id)) {
					employees.remove(i);
					System.out.println("Employee is deleted succesfully");
					present=true;
					break;
				}
			}
			
			if(!present) {
				throw new EmployeeDoesNotExists("Employee with id doesn't exist");
			}
			
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(employees.toJSONString());
			writer.flush();
			writer.close();
			}catch (EmployeeDoesNotExists e) {
			    System.out.println(e.getMessage());
			}catch(IOException e) {
			System.out.println(e.getMessage());
		   }catch(ParseException e) {
			   System.out.println(e.getMessage());
		   }

}
}
