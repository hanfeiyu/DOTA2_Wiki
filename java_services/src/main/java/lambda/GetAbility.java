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

public class GetAbility implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "GetAbility");

		// Check validations
		String AbilityName = null;
		if (request.containsKey("AbilityName")) {
			AbilityName = (String) request.get("AbilityName");
		} else {
			inspector.addAttribute("response", "Error: AbilityName shall not be null.");
			return inspector.finish();
		}

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
		String DB_TABLE = "Abilities";

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

			// Query data from database
			JSONObject result = new JSONObject();
			String query = "select * from " + DB_TABLE;

			// !All: select * from Abilities where AbilityName="xxx";
			if (!AbilityName.equals("All")) {
				query = query + " where AbilityName=\"" + AbilityName + "\"";
			}
			query = query + ";";

			JSONArray result_set = new JSONArray();

			// Execute the query and store result data
			ResultSet query_result = statement.executeQuery(query);

			while (query_result.next()) {
				JSONObject tuple = new JSONObject();
				tuple.put("AbilityName", query_result.getString("AbilityName"));
				tuple.put("HeroName", query_result.getString("HeroName"));
				tuple.put("AbilityType", query_result.getString("AbilityType"));
				tuple.put("CD", query_result.getInt("CD"));
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
