/**
 * http://usejsdoc.org/
 */

var zip_file_url = {
	"100": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/100_sales_records.csv",
	"1000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/1000_sales_records.csv",
	"5000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/5000_sales_records.csv",
	"10000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/10000_sales_records.csv",
	"50000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/50000_sales_records.csv",
	"100000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/100000_sales_records.csv",
	"500000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/500000_sales_records.csv",
	"1000000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/1000000_sales_records.csv",
	"1500000": "http://cssgate.insttech.washington.edu/~varikmp/tcss562/1500000_sales_records.csv"
}

var request = {
		"csv_url_file": zip_file_url["100"],
		"csv_file_name": "100_sales_records.csv",
		"sales_channel": "Online",
		"order_priority": "Critical"
}


function main(params) {
	//return (require('./extract'))(params);
	return (require('./load'))(params);
	//return (require('./query'))(params);
}

console.log(main(request));