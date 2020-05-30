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

public class GetCache implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "DeleteCache");

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

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Connection connection;
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			Statement statement = connection.createStatement();

			String query_use_db = "use " + DB_NAME + ";";
			statement.execute(query_use_db);
			
			String query = "select * from " + DB_TABLE;

			ResultSet query_result = statement.executeQuery(query);
			
			JSONObject result = new JSONObject();						
			JSONArray result_set = new JSONArray();
			
			while (query_result.next()) {
				JSONObject tuple = new JSONObject();
				tuple.put("HeroName", query_result.getString("HeroName"));
				tuple.put("PrimaryAttribute", query_result.getString("PrimaryAttribute"));
				tuple.put("Faction", query_result.getString("Faction"));
				tuple.put("Ability", query_result.getString("Ability"));
				tuple.put("Item", query_result.getString("Item"));
				tuple.put("Type", query_result.getString("Type"));
				tuple.put("Complexity", query_result.getString("Complexity"));
				tuple.put("WinningRate", query_result.getFloat("WinningRate"));
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
