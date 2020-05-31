/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local;

import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import lambda.GetRec;
import lambda.GetView;
import lambda.GetCache;
import lambda.PutCache;
import lambda.DropCache;
import lambda.DeleteCache;
import lambda.GetAbility;
import lambda.GetItem;
import lambda.GetPlayer;
import lambda.GetHero;

public class Local {

	// int main enables testing function from cmd line
	public static void main(String[] args) {
		Context c = new Context() {
			@Override
			public String getAwsRequestId() {
				return "";
			}

			@Override
			public String getLogGroupName() {
				return "";
			}

			@Override
			public String getLogStreamName() {
				return "";
			}

			@Override
			public String getFunctionName() {
				return "";
			}

			@Override
			public String getFunctionVersion() {
				return "";
			}

			@Override
			public String getInvokedFunctionArn() {
				return "";
			}

			@Override
			public CognitoIdentity getIdentity() {
				return null;
			}

			@Override
			public ClientContext getClientContext() {
				return null;
			}

			@Override
			public int getRemainingTimeInMillis() {
				return 0;
			}

			@Override
			public int getMemoryLimitInMB() {
				return 0;
			}

			@Override
			public LambdaLogger getLogger() {
				return new LambdaLogger() {
					@Override
					public void log(String string) {
						System.out.println("LOG:" + string);
					}
				};
			}
		};

		// Initialize request
		HashMap<String, Object> request = new HashMap<String, Object>();	

		
		//
		// GetView test
		//
		
		request.clear();
		request.put("api", "GetView");

		GetView getView = new GetView();
		HashMap<String, Object> getViewResult = getView.handleRequest(request, c);
		System.out.println("GetRec result:\n" + getViewResult.toString());
		
		//
		// GetRec test
		//
		
		request.clear();
		request.put("PrimaryAttribute", "All");
		request.put("Fraction", "All");
		request.put("HeroType", "All");
		request.put("Complexity", "All");
		request.put("PlayerName", "Zard-");
		
		GetRec getRec = new GetRec();
		HashMap<String, Object> getRecResult = getRec.handleRequest(request, c);
		System.out.println("GetRec result:\n" + getRecResult.toString());
    
		//
		// PutCache test
		//
		
		request.clear();
		request.put("HeroName", "All");
		
		PutCache putCache = new PutCache();
        HashMap<String, Object> putCacheResult = putCache.handleRequest(request, c);        
        System.out.println("PutCache result:\n" + putCacheResult.toString());

		//
		// GetCache test
		//
		
        request.clear();
		request.put("api", "GetCache");
		
		GetCache getCache = new GetCache();
        HashMap<String, Object> getCacheResult = getCache.handleRequest(request, c);        
        System.out.println("GetCache result:\n" + getCacheResult.toString());

		
		//
		// DropCache test
		//
        
        request.clear();
		request.put("api", "DropCache");
        
        DropCache dropCache = new DropCache();
        HashMap<String, Object> dropCacheResult = dropCache.handleRequest(request, c);     
        System.out.println("DropCache result:\n" + dropCacheResult.toString());
        
       
		//
		// DeleteCache test
		//
		
        request.clear();
		request.put("HeroName", "Huskar");
		
		DeleteCache deleteCache = new DeleteCache();
		HashMap<String, Object> deleteCacheResult = deleteCache.handleRequest(request, c);     
		System.out.println("DeleteCache result:\n" + deleteCacheResult.toString());
		
        //
        // GetHero test
        //
		
		request.clear();
		request.put("HeroName", "Huskar");
		
		GetHero getHero = new GetHero();
        HashMap<String, Object> getHeroResult = getHero.handleRequest(request, c);        
        System.out.println("GetHero result:\n" + getHeroResult.toString());
        
        //
        // GetAbility test
        //
        
        request.clear();
        request.put("AbilityName", "Sun Strike");
        
		GetAbility getAbility = new GetAbility();
        HashMap<String, Object> getAbilityResult = getAbility.handleRequest(request, c);        
        System.out.println("GetAbility result:\n" + getAbilityResult.toString());
        
        //
        // GetItem test
        //
        
        request.clear();
		request.put("ItemName", "Shadow Blade");

		GetItem getItem = new GetItem();
        HashMap<String, Object> getItemResult = getItem.handleRequest(request, c);        
        System.out.println("GetItem result:\n" + getItemResult.toString());
        
        //
        // GetPlayer test
        //
        
        request.clear();
		request.put("PlayerName", "Ana");
        
        GetPlayer getPlayer = new GetPlayer();
        HashMap<String, Object> getPlayerResult = getPlayer.handleRequest(request, c);        
        System.out.println("GetPlayer result:\n" + getPlayerResult.toString());
	}
}
