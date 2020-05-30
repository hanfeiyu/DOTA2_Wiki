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


public class GetItem implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {
	
	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {
        
    	Inspector inspector = new Inspector();
    	inspector.addAttribute("api", "GetItem");
    	
    	String ItemName = null;
        if (request.containsKey("ItemName")) {
        	ItemName = (String) request.get("ItemName");
        } else {
        	inspector.addAttribute("response", "Error: ItemName shall not be null.");
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
		String DB_TABLE1 = "Item_Price";
		String DB_TABLE2 = "Item_Upgrade";
		String DB_TABLE3 = "Price_ItemType";
		
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
			
			String query = "select * from " + DB_TABLE1;
			if (!ItemName.equals("All")) {
				query = query + " where ItemName=\"" + ItemName + "\"";
			}
	        query = query + ";";
	        
			JSONArray result_set = new JSONArray(); 
			ResultSet query_result = statement.executeQuery(query);
			
			while(query_result.next()) {
				JSONObject tuple = new JSONObject();
				tuple.put("ItemName", query_result.getString("ItemName"));	
				
				if(query_result.getInt("Price") == 0)
					tuple.put("Price", null);
				else
					tuple.put("Price", query_result.getInt("Price"));
				
				result_set.add(tuple);			
			}
			
			for(int i = 0; i < result_set.size(); i++) {
				JSONObject job = (JSONObject)result_set.get(i);    // work in org.json.simple				
				String queryItemType = null;
				
				if(job.get("Price") == null)
					queryItemType = "select ItemType from " + DB_TABLE3 + " where " + "Price=0 ;";
				else {
					Integer itemPrice = (Integer)job.get("Price");
					queryItemType = "select ItemType from " + DB_TABLE3 + " where " + "Price=" + itemPrice + ";";
				}
				
				ResultSet query_ItemType = statement.executeQuery(queryItemType);				
				String itemType = null;
				query_ItemType.next();
				itemType = query_ItemType.getString("ItemType");
				job.put("ItemType", itemType);
			}
			
			int size = result_set.size();
			
			JSONArray array = result_set;
			
			for(int i = 0; i < size; i++) {
				JSONObject job = (JSONObject)result_set.get(i); 
				String itemName = (String)job.get("ItemName");
				String queryUpgrade = "select Upgrade from " + DB_TABLE2 + " where " + "ItemName=\"" + itemName + "\";";
				ResultSet query_Upgrade = statement.executeQuery(queryUpgrade);
				String upgrade = null;
				while(query_Upgrade.next()) {
					JSONObject save = (JSONObject)result_set.get(i); 
					upgrade = query_Upgrade.getString("Upgrade");
					if (upgrade == null || upgrade.equals("null")) {
						save.put("Upgrade", null);
						} else { 
							save.put("Upgrade", upgrade);
						}
					array.add(new JSONObject(save));
				}
			}
			
			result.put("results", array);
	        
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
