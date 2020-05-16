from utilities import download_file, get_file_name_from_uri,\
        is_file_existed_on_memory, TEMP_DIRECTORY, PROCESSED_NAME,\
        download_if_file_exists_on_amazon_s3, upload_to_bucket,\
    is_database_existed, is_table_existed,\
    create_database, create_table, drop_table, get_insert_statement,\
    add_data_aggregations,\
    add_full_data_set
from error_handler import error_key_not_found, error_file_not_found, error_mysql
from inspector import Inspector

import pandas as pd
import pymysql
import datetime

config = dict()
# config["db_host"] = "localhost"
config["db_host"] = "sales.cluster-cfcyka432suz.us-east-1.rds.amazonaws.com"
config["db_url"] = "jdbc:mysql://{}:3306".format(config["db_host"])
config["db_driver"] = "com.mysql.cj.jdbc.Driver"
config["db_user"] = "varikmp"
config["db_passwd"] = "password"

# select TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE from TABLES;

#
# Service #1 - Extract and Transform the Data
#
# @param request A JSON object provided by the platform handler.
# @param context A platform specific object used to communicate with the cloud platform.
# @returns A JSON object to use as a response.
#
def extract(request, context):
    # Import the module and collect data
    inspector = Inspector()
    inspector.inspectAll()
    
    inspector.addAttribute("message", "extracting ...")
    
    # validations check up
    if 'csv_file_url' not in request:
        error_key_not_found(inspector)
        return inspector.finish()
     
     
    url = str(request['csv_file_url'])
    file_name = get_file_name_from_uri(url)
     
    # check if the file is still in cache
    if is_file_existed_on_memory(PROCESSED_NAME + file_name):
        print('processed file is still in the memory or temporary directory')
        inspector.addAttribute("response", "succeed")
        return inspector.finish()
         
    # check if the file is on amazon s3
    if download_if_file_exists_on_amazon_s3(PROCESSED_NAME + file_name):
        print('download the processed file from amazon s3')
        inspector.addAttribute("response", "succeed")
        return inspector.finish()
     
    print('prepare to process the file data')
    file_path = download_file(url)
    data = pd.read_csv(file_path)
     
    # remove duplicate data identified by [Order ID]
    data.drop_duplicates(subset ="Order ID", keep=False, inplace=True)
     
    # add column that stores an integer value representing
    # the number of days between order date and ship date
    order_processing_time = pd.to_datetime(data['Ship Date']) - pd.to_datetime(data['Order Date'])
    data['Order Processing Time'] = order_processing_time.apply(lambda x: x.days)
     
    # transform the priority from letter to text
    data['Order Priority'] = data['Order Priority'].apply(transform_priority)
     
    # the gross margin is a percentage calculated
    # by total profit divided by total revenue
    data['Gross Margin'] = data['Total Profit'] / data['Total Revenue']
     
    file_name = PROCESSED_NAME + file_name
    file_path = TEMP_DIRECTORY + file_name
     
    data.to_csv(file_path)
    upload_to_bucket(file_path)
            
#     print(list(data.columns))
#     print(data[['Order Processing Time', 'Order Priority', 'Gross Margin']])     
    
    inspector.addAttribute("response", "succeed")
    return inspector.finish()

def transform_priority(letter):
    if letter == 'L':
        return 'Low'
    elif letter == 'M':
        return 'Medium'
    elif letter == 'H':
        return 'High'
    elif letter == 'C':
        return 'Critical'
    else:
        return 'Unknown'

def load(request, context):
    # Import the module and collect data
    inspector = Inspector()
    inspector.inspectAll()
    
    inspector.addAttribute("message", "loading ...")

    # validations check up
    if 'csv_file_name' not in request:
        error_key_not_found(inspector)
        return inspector.finish()
    
    file_name = str(request['csv_file_name'])
    
    # check if the file is still in cache
    if is_file_existed_on_memory(PROCESSED_NAME + file_name):
        print('processed file is still in the memory or temporary directory')
    # check if the file is on amazon s3
    elif download_if_file_exists_on_amazon_s3(PROCESSED_NAME + file_name):
        print('download the processed file from amazon s3')
    else:
        error_file_not_found(inspector, file_name)
        return inspector.finish()

    connection = pymysql.connect(host=config["db_host"], user=config["db_user"], password=config["db_passwd"],
        db='mysql', charset='utf8mb4', cursorclass=pymysql.cursors.DictCursor)

    try:
        # check all valid conditions
        # while connecting to the database
        cursor = connection.cursor()
        if 'overwrite_table' in request and int(request['overwrite_table']) > 0 and is_table_existed(cursor):
            drop_table(cursor)
        if not is_database_existed(cursor, 'sales'):
            create_database(cursor, 'sales')
        if not is_table_existed(cursor):
            create_table(cursor)
        
        insert_template = get_insert_statement()

        # https://stackoverflow.com/questions/16476924/how-to-iterate-over-datas-in-a-dataframe-in-pandas
        data = pd.read_csv(TEMP_DIRECTORY + PROCESSED_NAME + file_name)
        for index in data.index: 
            insert_value = insert_template.format(
                data['Order ID'][index], # order_id,
                data['Region'][index], # region,
                data['Country'][index], # country,
                data['Item Type'][index], # item_type,
                data['Sales Channel'][index], # sales_channel,
                data['Order Priority'][index], # order_priority,
                datetime.datetime.strptime(data['Order Date'][index], '%m/%d/%Y').strftime('%Y-%m-%d'), # order_date,
                datetime.datetime.strptime(data['Ship Date'][index], '%m/%d/%Y').strftime('%Y-%m-%d'), # ship_date,
                data['Units Sold'][index], # unit_price,
                data['Unit Price'][index], # unit_price,
                data['Unit Cost'][index], # unit_cost,
                data['Total Revenue'][index], # total_revenue,
                data['Total Cost'][index], # total_cost,
                data['Total Profit'][index], # total_profit,
                data['Order Processing Time'][index], # order_processing_time,
                data['Gross Margin'][index], # gross_margin
            )
            cursor.execute(insert_value)
        connection.commit()
        inspector.addAttribute("response", "succeed")
    except Exception as e:
        error_mysql(inspector, "{}".format(e))
    finally:
        connection.close()

    return inspector.finish()

def query(request, context):
    # Import the module and collect data
    inspector = Inspector()
    inspector.inspectAll()
    
    inspector.addAttribute("message", "querying ...")

    # validations check up
    fields = dict()
    if 'region' in request:
        fields['region'] = request['region']
    if 'item_type' in request:
        fields['item_type'] = request['item_type']
    if 'sales_channel' in request:
        fields['sales_channel'] = request['sales_channel']
    if 'order_priority' in request:
        fields['order_priority'] = request['order_priority']
    if 'country' in request:
        fields['country'] = request['country']

    connection = pymysql.connect(host=config["db_host"], user=config["db_user"], password=config["db_passwd"],
        db='sales', charset='utf8mb4', cursorclass=pymysql.cursors.DictCursor)

    try:
        # check all valid conditions
        # while connecting to the database
        result = dict()
        cursor = connection.cursor()
        add_data_aggregations(result, cursor, fields),
        add_full_data_set(result, cursor, fields)
        inspector.addAttribute("response", result)
        
    except Exception as e:
        error_mysql(inspector, "{}".format(e))
    finally:
        connection.close()
    
    return inspector.finish()



