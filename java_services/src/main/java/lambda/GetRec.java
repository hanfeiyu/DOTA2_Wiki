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
    	String PlayerName = null;
    	
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
		String DB_PASSWORD = "wtwtwt123";
		String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
		String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
		String DB_NAME = "Dota2wiki";
		String DB_TABLE = "Heroes";
		String DB_PLAYER = "Players";
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
			
			if (!PrimaryAttribute.equals("All")) {
				createView = createView + " where PrimaryAttribute=\"" + PrimaryAttribute + "\"";
			}
			if (!Faction.equals("All")) {
				createView = createView + " and Faction=\"" + Faction + "\"";
			}
			if (!HeroType.equals("All")) {
				createView = createView + " and Type=\"" + HeroType + "\"";
			}
			if (!Complexity.equals("All")) {
				createView = createView + " and Complexity=\"" + Complexity + "\"";
			}
			
			// connection with DB_PLAYER
			if (!PlayerName.equals("All")) {
				createView = createView + " and HeroName in (select Representative from " + DB_PLAYER + " where "
						+ "PlayerName=\"" + PlayerName + "\")";
			}
						
			createView = createView + ";";
			
			statement.executeLargeUpdate(createView);
	
			// Query date from view
			String query = "select * from " + VIEW_HERO + ";"; 
			
			// Execute the query and store result data
			ResultSet query_result = statement.executeQuery(query);  
			JSONObject result = new JSONObject();			

			// query_result empty or not
			if(!query_result.isBeforeFirst()) {
				result.put("results", null);	  // output format: response={"results":null}		
				System.out.println("No result in query");
			}
			else {
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
				
				
				// traversal of the result_set
				for(int i = 0; i < result_set.size(); i++) {
					JSONObject job = (JSONObject)result_set.get(i);    // work in org.json.simple
	//				System.out.println(job.get("HeroName"));
					
	//				System.out.println(job.get("PlayerName"));
					String heroName = (String)job.get("HeroName");
	//				System.out.println(heroName);
	
					String queryPlayer = "select PlayerName from " + DB_PLAYER + " where " + "Representative=\"" + heroName + "\";";
	//				System.out.println(queryPlayer);				
	
					ResultSet query_playerName = statement.executeQuery(queryPlayer);
	//				System.out.println(query_playerName);
					
					
					String playerName = null;
	
					while(query_playerName.next()) {
						playerName = query_playerName.getString("PlayerName");
					}
					
	//				System.out.println(playerName);
					
					job.put("PlayerName", playerName);
				}
				
//				
//				if(result_set.size() == 0)  // is here correct??? need test
//					result.compute("results", null);
//				else			
				result.put("results", result_set);

			
			// Drop the view
			// Delete the view from database
//			String delete = "drop view " + VIEW_HERO + ";";
//
//			// Execute the delete
//			statement.executeLargeUpdate(delete);
			}
			
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
