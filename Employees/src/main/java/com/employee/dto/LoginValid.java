package com.employee.dto;

import java.io.File;
import java.io.FileReader;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.employee.exceptions.InvalidIdException;
import com.employee.dao.HashPassword;
import com.employee.exceptions.LoginFailedException;

public class LoginValid {

    public static String role;
    public static String id;

    public static boolean login(String id, String password) {

        try {
            File file = new File("output.json");

            if (!file.exists() || file.length() <= 2) {
                throw new LoginFailedException("No login records found");
            }

            JSONParser parser = new JSONParser();
            JSONArray usersArray =
                    (JSONArray) parser.parse(new FileReader(file));

            String hashedInputPassword =
                    HashPassword.hashPassword(password);

            for (Object obj : usersArray) {
                JSONObject user = (JSONObject) obj;

                String jsonId = (String) user.get("id");
                String jsonPassword = (String) user.get("password");

                if (jsonId.equals(id)
                        && jsonPassword.equals(hashedInputPassword)) {

                    LoginValid.id = jsonId;

                    JSONArray roleArray = (JSONArray) user.get("role");

                    roleArray.sort((o1, o2) ->
                            o1.toString().compareTo(o2.toString()));

                    LoginValid.role = roleArray.get(0).toString();

                    System.out.println("Login Successful");
                    return true;
                }
            }

            
            throw new InvalidIdException("Invalid ID or Password");

        } catch (LoginFailedException e) {
          System.out.println(e.getMessage()); 
        } catch(InvalidIdException e) {
        	 System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return false;
    }
}
