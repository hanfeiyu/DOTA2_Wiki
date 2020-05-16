/**
 * 
 */

const fs = require("fs");
const req = require("request");
const aws = require("aws-sdk");
const parse = require("csv-parse/lib/sync");
const util = require("./utilities");

const url = "http://cssgate.insttech.washington.edu/~varikmp/tcss562/100_sales_records.csv";


function get_file_name_from_string(url) {
	let list = url.split("/");
	return list.pop();
}

function download_file(url) {
    let file_name = get_file_name_from_string(url);
    let file_path = TEMP_DIRECTORY + file_name;
    
	if (!fs.existsSync(file_path)) {
        console.log(file_path + " does not exist.");

        let stream = fs.createWriteStream(file_path);
        console.log("Retrieving HTTP meta-data...");
        req(url).pipe(stream).on("close", function (err) {
            console.log("[" + file_name + "] downloaded successfully.");
        });
	} else {
		console.log("Loading " + file_name + " from cache...");
	}

    return file_path;
}

function is_file_existed_on_memory(file_name) {
	let file_path = TEMP_DIRECTORY + file_name;

	if (fs.existsSync(file_path)) {
		return true;
	} else {
		return false;
	}
}

function is_bucket_existed(bucket_name, callback) {
	let s3 = new aws.S3();
	
	s3.listBuckets({}, function(err, data) {
		let flag = false;
		
		if (err) {
			console.log("Error in calling is_bucket_existed");
			console.log(err, err.stack); 
		} else {
			bucket_list = data["Buckets"];				
			for (let bucket_name of bucket_list) {
				if (bucket_name["Name"] == PROJECT_BUCKET) {
					flag = true;
				}
			}
		}
		
		callback(flag);
	});
}

function upload_to_bucket(file_path) {
	let s3 = new aws.S3();
	is_bucket_existed(PROJECT_BUCKET, function(result) {
		if (!result) {
			let params = {
				Bucket: PROJECT_BUCKET,
				CreateBucketConfiguration: {
					LocationConstraint: LOCATION_CONSTRAINT
				}
			};
				
			console.log("Creating a new bucket");
	        s3.createBucket(params, function(err, data) {
	        	if (err) {
	        		console.log(err, err.stack); // an error occurred
	        	} else {
	        		console.log(data);           // successful response
	        	}
	        });
		}
		
		let file_name = get_file_name_from_string(file_path);
		let params = {
			Bucket: PROJECT_BUCKET,
			Key: file_name,
			Body: fs.createReadStream(file_path)
		}
		
		console.log("Uploading data");
		s3.upload(params, function(err, data) {
			if (err) {
				console.log("Error in upload_to_bucket");
				console.log(err, err.stack);
			} else {
				console.log(data);
			}
		});
	});
}

let file_name = util.get_file_name_from_string(url);

let file = fs.readFileSync(util.TEMP_DIRECTORY + util.PROCESSED_NAME + file_name);
let json_file = parse(file, {
    columns: true,
    skip_empty_lines: true
});

//console.log(json_file);

for (let i=0; i<json_file.length; i++) {
    	
	let insert_value = "insert into sales.sales_records " +
			"(" +
			"order_id, " +
			"region, " +
			"country, " +
			"item_type, " +
			"sales_channel, " +
			"order_priority, " +
			"order_date, " +
			"ship_date, " +
			"unit_sold, " +
			"unit_price, " +
			"unit_cost, " +
			"total_revenue, " +
			"total_cost, " +
			"total_profit, " +
			"order_processing_time, " +
			"gross_margin" +
			") " +
			"values" +
			"(" +
			"\"" + json_file[i]["Order ID"] + "\", " + 
			"\"" + json_file[i]["Region"] + "\", " +
			"\"" + json_file[i]["Country"] + "\", " +
			"\"" + json_file[i]["Item Type"] + "\", " +
			"\"" + json_file[i]["Sales Channel"] + "\", " +
			"\"" + json_file[i]["Order Priority"] + "\", " +
			"\"" + json_file[i]["Order Date"] + "\", " +
			"\"" + json_file[i]["Ship Date"] + "\", " +
			"\"" + json_file[i]["Units Sold"] + "\", " +
			"\"" + json_file[i]["Unit Price"] + "\", " +
			"\"" + json_file[i]["Unit Cost"] + "\", " +
			"\"" + json_file[i]["Total Revenue"] + "\", " +
			"\"" + json_file[i]["Total Cost"] + "\", " +
			"\"" + json_file[i]["Total Profit"] + "\", " +
			"\"" + json_file[i]["Order Processing Time"] + "\", " +
			"\"" + json_file[i]["Gross Margin"] + "\"," +
			");";
	connection.query(insert_value);
}



