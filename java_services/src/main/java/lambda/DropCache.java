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

public class DropCache implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "DropCache");

		// Check validations
		// no need
//    	String AbilityName = null;
//        if (request.containsKey("AbilityName")) {
//        	AbilityName = (String) request.get("AbilityName");
//        } else {
//        	inspector.addAttribute("response", "Error: AbilityName shall not be null.");
//        	return inspector.finish();
//        }

		// Get environmnet variables
//    	String DB_URL = System.getenv("DB_URL");
//    	String DB_USERNAME = System.getenv("DB_USERNAME");
//    	String DB_PASSWORD = System.getenv("DB_PASSWORD");
//    	String DB_NAME = System.getenv("DB_NAME");
//    	String DB_TABLE = System.getenv("DB_TABLE");
		String DB_USERNAME = "root";
		String DB_PASSWORD = "wtwtwt123";
		String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
		String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
		String DB_NAME = "Dota2wiki";
		String DB_TABLE = "HeroesCache";

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

			// Use designated table
			String query_use_db = "use " + DB_NAME + ";";
			statement.execute(query_use_db);

			// Truncate data from database
			String query = "Truncate table " + DB_TABLE;

			// Execute the query and store result data
			statement.executeUpdate(query);

			statement.close();
			connection.close();

			inspector.addAttribute("response", "Drop the cache successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
			inspector.addAttribute("response", "Error: Failed to query data from database.");
		}

		return inspector.finish();
	}
}
