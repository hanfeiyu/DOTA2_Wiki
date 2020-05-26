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

public class PutCache implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "PutCache");

		// Check validations
//		if (request.get("HeroName").equals("")) {
//			inspector.addAttribute("response", "Error: No new recommended hero need to be added.");
//			return inspector.finish();
//		}

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
		String DB_NAME = "DOTA2_Wiki";
		String DB_TABLE = "HeroesCache";
		String VIEW_HERO = "tempHero";

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

			// Insert into cache of data from view & delete the view
			String insert = "insert into " + DB_TABLE + " select * from " + VIEW_HERO + ";";
			String delete = "drop view " + VIEW_HERO + ";";
			
			// insert & check validations
			try {
				statement.executeUpdate(insert);
				statement.executeLargeUpdate(delete);  // in case of duplicate
			} catch (SQLException e) {}
			
			// Query data
			String query = "select * from " + DB_TABLE + ";";
			
			JSONObject result = new JSONObject();
			JSONArray result_set = new JSONArray();

			// Execute the query
			ResultSet query_result = statement.executeQuery(query);
			
//			if(!(query_result.getString("HeroName").contentEquals(""))) { // in case no recommendation
				while (query_result.next()) {
					JSONObject tuple = new JSONObject();
					tuple.put("HeroName", query_result.getString("HeroName"));
					tuple.put("PrimaryAttribute", query_result.getString("PrimaryAttribute"));
					tuple.put("Faction", query_result.getString("Faction"));
					tuple.put("Ability", query_result.getString("Ability"));
					tuple.put("Item", query_result.getString("Item"));
					tuple.put("Type", query_result.getString("Type"));
					tuple.put("Complexity", query_result.getInt("Complexity"));
					tuple.put("WinningRate", query_result.getFloat("WinningRate"));
					result_set.add(tuple);
				}
				result.put("results", result_set);
//			}

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
