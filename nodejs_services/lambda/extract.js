
/**
 * Define your FaaS Function here.
 * Each platform handler will call and pass parameters to this function.
 *
 * @param request A JSON object provided by the platform handler.
 * @param context A platform specific object used to communicate with the platform.
 * @returns A JSON object to use as a response.
 */

// Import modules
const inspector = new (require('./Inspector'))();
const util = require("./utilities");

//
// Service 1: extract and transform
//

module.exports = function(request, context) {
        
    inspector.addAttribute("message", "extracting...");

    // Validations check up
    //if ("csv_file_url" not in request) {

    var url = request["csv_url_file"];
    var file_name = util.get_file_name_from_string(url);

    // Check if the file is still in cache
    if (util.is_file_existed_on_memory(util.PROCESSED_NAME + file_name)) {
        console.log("Processed file is still in the memory or temporary directory");
        return inspector.finish();
    }
         
    // Check if the file is on amazon s3
    if (util.download_if_file_exists_on_AWS_S3(util.PROCESSED_NAME + file_name)) {
        console.log("Download the processed file from AWS S3");
        return inspector.finish();
    }

    // Read CSV input file
    console.log("Prepare to process the file data");
    var file_path = util.download_file(url);
    var processed_file_path = util.generate_new_csv_file(file_path, file_name);

    util.upload_to_bucket(processed_file_path);
    
    return inspector.finish();
    
    //inspector.inspectCPUDelta()
};
