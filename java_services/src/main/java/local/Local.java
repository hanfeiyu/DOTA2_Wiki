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
//import lambda.GetAbility;
//import lambda.GetItem;
//import lambda.GetPlayer;


public class Local {
	
    // int main enables testing function from cmd line
    public static void main (String[] args)
    {
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
		request.put("HeroName", "Huskar");
		request.put("HeroName", "All");
		request.put("AbilityName", "Sun Strike");
//		request.put("AbilityName", "All");
		request.put("ItemName", "Ocean Heart");
//		request.put("ItemName", "All");
		request.put("PlayerName", "XG");
//		request.put("PlayerName", "All");
        
        //
        // GetHero test
        //
        
		GetRec getRec = new GetRec();
        HashMap<String, Object> getRecResult = getRec.handleRequest(request, c);        
        System.out.println("GetRec result:\n" + getRecResult.toString());
//        
        //
        // GetAbility test
        //
        
//		GetAbility getAbility = new GetAbility();
//        HashMap<String, Object> getAbilityResult = getAbility.handleRequest(request, c);        
//        System.out.println("GetAbility result:\n" + getAbilityResult.toString());
        
        //
        // GetItem test
        //
        
//		GetItem getItem = new GetItem();
//        HashMap<String, Object> getItemResult = getItem.handleRequest(request, c);        
//        System.out.println("GetItem result:\n" + getItemResult.toString());
//        
//        //
//        // GetPlayer test
//        //
//        
//        GetPlayer getPlayer = new GetPlayer();
//        HashMap<String, Object> getPlayerResult = getPlayer.handleRequest(request, c);        
//        System.out.println("GetPlayer result:\n" + getPlayerResult.toString());

    }
}
