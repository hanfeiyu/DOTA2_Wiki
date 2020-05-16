'''
Created on Nov 24, 2019

@author: varikmp
'''
from handler import extract, load, query

my_request = dict()
  
# http://faculty.washington.edu/wlloyd/courses/tcss562/project/etl/sales_data/

data_size = '100' # '100', '1000', '5000', '10000', '50000', '100000', '500000', '1000000', '1500000'
file_pattern = "{}_sales_records.csv"
my_request["csv_file_name"] = file_pattern.format(data_size)
csv_link = 'http://cssgate.insttech.washington.edu/~varikmp/tcss562/' + my_request["csv_file_name"]
my_request["csv_file_url"] = csv_link
my_request['overwrite_table'] = 1 # > 0 to overwrite the table

my_request['region'] = 'Asia'
my_request['sales_channel'] = 'Online'

# print(extract(my_request, True))
# print(load(my_request, True))
print(query(my_request, True))

# from utilities import is_bucket_existed, list_all_buckets
# print(list_all_buckets())
# print(is_bucket_existed('tcss562.mylogs.vmp'))

# from utilities import upload_to_bucket
# upload_to_bucket('/tmp/100_sales_records.csv')


# from utilities import download_if_file_exists_on_amazon_s3
# download_if_file_exists_on_amazon_s3('processed_100_sales_records.csv')

