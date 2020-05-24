package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import saaf.Inspector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class GetPlayer implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {
	
	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {
        
    	Inspector inspector = new Inspector();
    	inspector.addAttribute("api", "GetPlayer");
    	
    	// Check validations
    	String PlayerName = null;
        if (request.containsKey("PlayerName")) {
        	PlayerName = (String) request.get("PlayerName");
        } else {
        	inspector.addAttribute("response", "Error: PlayerName shall not be null.");
        	return inspector.finish();
        }
        
    	// Get environmnet variables
//    	String DB_URL = System.getenv("DB_URL");
//    	String DB_USERNAME = System.getenv("DB_USERNAME");
//    	String DB_PASSWORD = System.getenv("DB_PASSWORD");
//    	String DB_NAME = System.getenv("DB_NAME");
//    	String DB_TABLE = System.getenv("DB_TABLE");
    	String DB_USERNAME = "root";
        String DB_PASSWORD = "wtwt123";
        String DB_URL = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&serverTimezone=GMT";
    	String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    	String DB_NAME = "DOTA2_Wiki";
    	String DB_TABLE = "Players";
        
    	// Register database driver
    	try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	// Query data from database
		try {
			Connection connection;
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			Statement statement = connection.createStatement();
			
			String query_use_db = "use " + DB_NAME + ";";	
			statement.execute(query_use_db);
			
	        // Query data from database
			JSONObject result = new JSONObject();
			String query = "select * from " + DB_TABLE;
			if (!PlayerName.equals("All")) {
				query = query + " where PlayerName=\"" + PlayerName + "\"";
			}
	        query = query + ";";
	        
			JSONArray result_set = new JSONArray(); 
	        
	        // Execute the query and store result data
			ResultSet query_result = statement.executeQuery(query);
			
			while (query_result.next()) {
				JSONObject tuple = new JSONObject();
				tuple.put("PlayerName", query_result.getString("PlayerName"));
				tuple.put("Representative", query_result.getString("Representative"));
				tuple.put("Team", query_result.getString("Team"));
				tuple.put("FamousScene", query_result.getString("FamousScene"));
				result_set.add(tuple);
			}
			result.put("results", result_set);
	        
			statement.close();
			connection.close();
	        
			inspector.addAttribute("response", result);
			
		} catch (SQLException e) {
			e.printStackTrace();
			inspector.addAttribute("response", "Error: Failed to query data from database.");
		}
        
    	return inspector.finish();
    }
}
