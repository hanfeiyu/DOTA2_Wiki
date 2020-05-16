/**
 * 
 */

// Import modules
const inspector = new (require('./Inspector'))();
const util = require("./utilities");
const mysql = require("mysql");
//const fs = require("fs");
//const parse = require("csv-parse/lib/sync");
const async = require("async");


//
//Service 3: query
//

module.exports = function(request, context, callback) {
    
    inspector.addAttribute("message", "querying...");

    // validations check up
    let fields = [];
    
    if (request.hasOwnProperty("region")) {
    	fields["region"] = request["region"];
    }
        
    if (request.hasOwnProperty("item_type")) {
    	fields["item_type"] = request["item_type"];
    }
    
    if (request.hasOwnProperty("sales_channel")) {
    	fields["sales_channel"] = request["sales_channel"];
    }
    
    if (request.hasOwnProperty("order_priority")) {
    	fields["order_priority"] = request["order_priority"];
    }
    
    if (request.hasOwnProperty("country")) {
    	fields["country"] = request["country"];
    }
    
	// Connect to database
	let connection = mysql.createConnection({
		host: util.config["db_host"],
		user: util.config["db_user"],
		password: util.config["db_passwd"],
		port: util.config["db_port"],
		database: util.config["db_name"]
	})
	
	connection.connect();
	console.log("Login database");
	console.log("Executing queries...");
	
	// Execute query operation
	let result = await util.add_full_data_set(connection, fields);
	//console.log(result);  
	connection.end();
	
	inspector.addAttribute("response", result);
	return inspector.finish();
	
	/*
	let query_string = util.add_data_aggregations(fields);
    
	connection.query(query_string, function(err, data) {
		if (err) {
			console.log(err);
		} else {
    		let result = [];
    		
    		result['avg_order_processing_time'] = data[0]['avg_order_processing_time'];
    	    result['avg_gross_margin'] = data[0]['avg_gross_margin'];
    	    result['avg_unit_sold'] = data[0]['avg_unit_sold'];
    	    result['max_unit_sold'] = data[0]['max_unit_sold'];
    	    result['min_unit_sold'] = data[0]['min_unit_sold'];
    	    result['total_unit_sold'] = data[0]['total_unit_sold'];
    	    result['total_total_revenue'] = data[0]['total_total_revenue'];
    	    result['total_total_profit'] = data[0]['total_total_profit'];
    	    result['total_orders'] = data[0]['total_orders'];
    	    
    	    console.log(result);
    	    
		    connection.end();
		    inspector.addAttribute("response", result);
		    return inspector.finish();
		}
    });
    */
}