/**
 * 
 */

// Import modules
const inspector = new (require('./Inspector'))();
const util = require("./utilities");
const mysql = require("mysql");
//const fs = require("fs");
//const parse = require("csv-parse/lib/sync");
//const async = require("async");

// Set up configuration
var config = [];
config["db_port"] = "3306";
config["db_host"] = "localhost";
config["db_user"] = "root";
config["db_passwd"] = "yhf3012523";


//
//Service 2: load
//

module.exports = function(request, context) {
    
    inspector.addAttribute("message", "loading...");

    // Validations check up
    //if ("csv_file_name" not in request) {

    var file_name = request["csv_file_name"];
    
    // Check if the file is still in cache
    if (util.is_file_existed_on_memory(util.PROCESSED_NAME + file_name)) {
        console.log("Processed file is still in the memory or temporary directory");
    }
    // Check if the file is on amazon s3
    else if (util.download_if_file_exists_on_AWS_S3(util.PROCESSED_NAME + file_name)) {
        console.log("Download the processed file from AWS S3");
    } 
	else {
		//error_file_not_found(inspector, file_name)
        return inspector.finish();
    }
	
	// Connect to database
	let connection = mysql.createConnection({
		host: util.config["db_host"],
		user: util.config["db_user"],
		password: util.config["db_passwd"],
		port: util.config["db_port"]
	})
	
	connection.connect();
	console.log("Login database");
	
	/*
	// Check all valid conditions while connecting to the database
    if (request.hasOwnProperty("overwrite_table") && Number(request['overwrite_table']) > 0) {
    	util.drop_table(connection);
    }
       
    async.waterfall([
        function(callback){
        	is_database_existed = util.is_database_existed(connection, "sales");
        	console.log("1" + is_database_existed);
            callback(null, is_database_existed);
        },
        function(is_database_existed,callback){
        	console.log("2" + is_database_existed);
        	if (!is_database_existed) {
        		util.create_database(connection, 'sales');
        	}
        },
        function(callback){
        	console.log("3");
        	is_table_existed = util.is_table_existed(connection);
        	callback(null, is_table_existed);
        },
        function(is_table_existed,callback){
        	if (!is_table_existed) {
        		util.create_table(connection);
        	}
        },
    ], function (err,result) {
   		console.log(result)
    });
    
	let insert_file = util.get_insert_statement();
  
    for (let index of data) { 
        insert_value = insert_template.format(
            data['Order ID'][index], // order_id,
            data['Region'][index], // region,
            data['Country'][index], // country,
            data['Item Type'][index], // item_type,
            data['Sales Channel'][index], // sales_channel,
            data['Order Priority'][index], // order_priority,
            datetime.datetime.strptime(data['Order Date'][index], '%m/%d/%Y').strftime('%Y-%m-%d'), // order_date,
            datetime.datetime.strptime(data['Ship Date'][index], '%m/%d/%Y').strftime('%Y-%m-%d'), // ship_date,
            data['Units Sold'][index], // unit_price,
            data['Unit Price'][index], // unit_price,
            data['Unit Cost'][index], // unit_cost,
            data['Total Revenue'][index], // total_revenue,
            data['Total Cost'][index], // total_cost,
            data['Total Profit'][index], // total_profit,
            data['Order Processing Time'][index], // order_processing_time,
            data['Gross Margin'][index], // gross_margin
        )
        connection.query(insert_value);
    }
    connection.commit()
    inspector.addAttribute("response", "succeed")
*/
	
	util.insert_CSV_into_database(connection, util.TEMP_DIRECTORY + util.PROCESSED_NAME + file_name);
	connection.commit();
	connection.end();
	
	inspector.addAttribute("response", "load succeed");
	
    return inspector.finish();
    
    //inspector.inspectCPUDelta()
};
