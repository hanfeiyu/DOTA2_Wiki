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


//
// Query Hero information from database
//

public class GetHero implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "GetHero");
		
    	//
    	// Check validations
    	//
		
		String HeroName = null;
		if (request.containsKey("HeroName")) {
			HeroName = (String) request.get("HeroName");
		} else {
			inspector.addAttribute("response", "Error: HeroName shall not be null.");
			return inspector.finish();
		}

		//
		// CLOUD DEPLOYMENT: get environment variables
		//
		
		String DB_ENDPOINT = System.getenv("DB_ENDPOINT");
	    String DB_USERNAME = System.getenv("DB_USERNAME");
	    String DB_PASSWORD = System.getenv("DB_PASSWORD");
	    String DB_NAME = System.getenv("DB_NAME");
	    String DB_URL = "jdbc:mysql://" + DB_ENDPOINT + ":3306/" + DB_NAME + "?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
	  
	    //
	    // LOCAL DEPLOYMENT: hardcoded variables
	    //
	    
		//  String DB_NAME = "Dota2wiki";
		//  String DB_USERNAME = "root";
		//  String DB_PASSWORD = "yhf3012523";
		//  String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
	  
	    String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
		String DB_TABLE = "Heroes";
		
		//
		// Connect to mysql
		//
		
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

			JSONObject result = new JSONObject();
			String query = "select * from " + DB_TABLE;
			if (!HeroName.equals("All")) {
				query = query + " where HeroName=\"" + HeroName + "\"";
			}
			query = query + ";";

			JSONArray result_set = new JSONArray();
			ResultSet query_result = statement.executeQuery(query);

			// add Player
			
			while (query_result.next()) {
				JSONObject tuple = new JSONObject();
				tuple.put("HeroName", query_result.getString("HeroName"));
				tuple.put("PrimaryAttribute", query_result.getString("PrimaryAttribute"));
				tuple.put("Fraction", query_result.getString("Fraction"));
				tuple.put("Ability", query_result.getString("Ability"));
				tuple.put("Item", query_result.getString("Item"));
				tuple.put("Type", query_result.getString("Type"));
				tuple.put("Complexity", query_result.getString("Complexity"));
				tuple.put("WinningRate", query_result.getFloat("WinningRate"));
				
				result_set.add(tuple);
			}
			
			for(int i = 0; i < result_set.size(); i++) {
				JSONObject job = (JSONObject)result_set.get(i);    // work in org.json.simple				
				String queryPlayerName = null;
				
				String heroName = (String)job.get("HeroName");
				queryPlayerName = "select PlayerName from Players where Representative=\"" + heroName + "\";";
				
				ResultSet query_HeroName = statement.executeQuery(queryPlayerName);				
				String playerName = null;
				query_HeroName.next();
				playerName = query_HeroName.getString("PlayerName");
				job.put("PlayerName", playerName);
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
