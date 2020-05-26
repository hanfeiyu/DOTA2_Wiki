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

public class GetRec implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "GetRec"); // get all

		// Check validations
    	String PrimaryAttribute = null;
    	String Faction = null;
    	String HeroType = null;
    	String Complexity = null;
    	String WinningRate = null;
    	
        if (request.containsKey("PrimaryAttribute")) {
        	PrimaryAttribute = (String) request.get("PrimaryAttribute");
        } else {
        	inspector.addAttribute("response", "Error: PrimaryAttribute shall not be null.");
        	return inspector.finish();
        }

        if (request.containsKey("Faction")) {
        	Faction = (String) request.get("Faction");
        } else {
        	inspector.addAttribute("response", "Error: Faction shall not be null.");
        	return inspector.finish();
        }
        
        if (request.containsKey("HeroType")) {
        	HeroType = (String) request.get("HeroType");
        } else {
        	inspector.addAttribute("response", "Error: HeroType shall not be null.");
        	return inspector.finish();
        }
        
        if (request.containsKey("Complexity")) {
        	Complexity = (String) request.get("Complexity");
        } else {
        	inspector.addAttribute("response", "Error: Complexity shall not be null.");
        	return inspector.finish();
        }
        
        if (request.containsKey("WinningRate")) {
        	WinningRate = (String) request.get("WinningRate");
        } else {
        	inspector.addAttribute("response", "Error: WinningRate shall not be null.");
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
		String DB_NAME = "DOTA2_Wiki";
		String DB_TABLE = "Heroes";
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
			
			// Delete the view from database
			String delete1 = "drop view " + VIEW_HERO + ";";
//			statement.executeLargeUpdate(delete1);

			// Execute the delete, in case of duplicate delete
			try {
				statement.executeLargeUpdate(delete1);
			} catch(SQLException e) {}
			

			
			// Get the REC			
			
			// Create view & Query data from database
			String createView = "create view " + VIEW_HERO + " as select * from " + DB_TABLE;
			
			if (!PrimaryAttribute.equals("")) {
				createView = createView + " where PrimaryAttribute=\"" + PrimaryAttribute + "\"";
			}
			if (!Faction.equals("")) {
				createView = createView + " and Faction=\"" + Faction + "\"";
			}
			if (!HeroType.equals("")) {
				createView = createView + " and Type=\"" + HeroType + "\"";
			}
			if (!Complexity.equals("")) {
				createView = createView + " and Complexity=\"" + Complexity + "\"";
			}
			if (!WinningRate.equals("")) {
				createView = createView + " and WinningRate=\"" + WinningRate + "\"";
			}
			createView = createView + ";";
			
			statement.executeLargeUpdate(createView);
			
			// Query date from view
			JSONObject result = new JSONObject();
			JSONArray result_set = new JSONArray();

			String query = "select * from " + VIEW_HERO + ";";

			// Execute the query and store result data
			ResultSet query_result = statement.executeQuery(query);

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

			
			// Drop the view
			// Delete the view from database
//			String delete = "drop view " + VIEW_HERO + ";";
//
//			// Execute the delete
//			statement.executeLargeUpdate(delete);
			
			
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
