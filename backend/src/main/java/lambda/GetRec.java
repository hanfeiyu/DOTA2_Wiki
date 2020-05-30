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
// Generate Recommendation view
//

public class GetRec implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		Inspector inspector = new Inspector();
		inspector.addAttribute("api", "GetRec");
		
    	//
    	// Check validations
    	//
		
    	String PrimaryAttribute = null;
    	String Fraction = null;
    	String HeroType = null;
    	String Complexity = null;
    	String PlayerName = null;

        if (request.containsKey("PrimaryAttribute")) {
        	PrimaryAttribute = (String) request.get("PrimaryAttribute");
        } else {
        	inspector.addAttribute("response", "Error: PrimaryAttribute shall not be null.");
        	return inspector.finish();
        }

        if (request.containsKey("Fraction")) {
        	Fraction = (String) request.get("Fraction");
        } else {
        	inspector.addAttribute("response", "Error: Fraction shall not be null.");
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
		String DB_PLAYER = "Players";
		String VIEW_HERO = "tempHero";
		
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
			
			String delete1 = "drop view " + VIEW_HERO + ";";

			try {
				statement.executeLargeUpdate(delete1);
			} catch(SQLException e) {}
			

			String createView = "create view " + VIEW_HERO + " as select * from " + DB_TABLE;
			
			if (!PrimaryAttribute.equals("All")) {
				createView = createView + " where PrimaryAttribute=\"" + PrimaryAttribute + "\"";
			}
			if (!Fraction.equals("All") && !PrimaryAttribute.equals("All")) {
				createView = createView + " and Fraction=\"" + Fraction + "\"";
			} else if(!Fraction.equals("All")) {
				createView = createView + " where Fraction=\"" + Fraction + "\"";				
			}
			if (!HeroType.equals("All") && (!PrimaryAttribute.equals("All") || !Fraction.equals("All"))) {
				createView = createView + " and Type=\"" + HeroType + "\"";
			} else if(!HeroType.equals("All")) {
				createView = createView + " where Type=\"" + HeroType + "\"";				
			}
			if (!Complexity.equals("All") && ((!PrimaryAttribute.equals("All") || !Fraction.equals("All")) || !HeroType.equals("All"))) {
				createView = createView + " and Complexity=\"" + Complexity + "\"";
			} else if(!Complexity.equals("All")) {
				createView = createView + " where Complexity=\"" + Complexity + "\"";				
			}
			if (!PlayerName.equals("All") && (((!PrimaryAttribute.equals("All") || !Fraction.equals("All")) || !HeroType.equals("All")) || !Complexity.equals("All"))) {
				createView = createView + " and HeroName in (select Representative from " + DB_PLAYER + " where "
						+ "PlayerName=\"" + PlayerName + "\")";
			} else if(!PlayerName.equals("All")) {
				createView = createView + " where HeroName in (select Representative from " + DB_PLAYER + " where "
						+ "PlayerName=\"" + PlayerName + "\")";				
			}
						
			createView = createView + ";";
			
			// add a column into view of PlayerName
			
			statement.executeLargeUpdate(createView);
				
			statement.close();
			connection.close();
			inspector.addAttribute("response", "Get the VIEW of Rec successfullu.");

		} catch (SQLException e) {
			e.printStackTrace();
			inspector.addAttribute("response", "Error: Failed to query data from database.");
		}
		return inspector.finish();
	}
}
