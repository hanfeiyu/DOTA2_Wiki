'''
Created on Nov 24, 2019

@author: varikmp
'''

import os
import pip
import boto3

import requests # this one is just used for local machine
import botocore

PROCESSED_NAME = 'processed_'
TEMP_DIRECTORY = '/tmp/'
PROJECT_BUCKET = 'project.bucket.tcss562.vmp'
LOCATION_CONSTRAINT = 'us-east-1'

@DeprecationWarning
def fast_install(package):
    if hasattr(pip, 'main'):
        pip.main(['install', package])
    else:
        from pip._internal import main
        pip._internal.main(['install', package])

@DeprecationWarning
def fast_import(package_string):
    try:
        __import__(package_string)
    except ImportError:
        try:
            fast_install(package_string)
            __import__(package_string)
        except ImportError:
            print(package_string + ' has been installed')

def encode_string_literals(string):
    return string.replace('"', '\\"').replace('\'', '"')

def get_file_name_from_uri(uri):
    return uri[uri.rfind("/") + 1:]

def download_file(url):
    file_name = get_file_name_from_uri(url)
    file_path = TEMP_DIRECTORY + file_name
    if os.path.isfile(file_path):
        print('Loading ' + file_name + ' from cache...')
    else:
        req = requests.get(url)
        with open(file_path, 'wb') as file_reader:
            file_reader.write(req.content)
        print('Retrieving HTTP meta-data...')
        print(req.status_code)
        print(req.headers['content-type'])
        print(req.encoding)
    return file_path

def is_file_existed_on_memory(file_name):
    file_path = TEMP_DIRECTORY + file_name
    if os.path.isfile(file_path):
        return True
    return False

def download_if_file_exists_on_amazon_s3(file_name):
    s3 = boto3.resource('s3')
    try:
        s3.Bucket(PROJECT_BUCKET).download_file(file_name, TEMP_DIRECTORY + file_name)
        return True
    except botocore.exceptions.ClientError:
        return False

def list_all_buckets():
    s3 = boto3.client('s3')
    response = s3.list_buckets()
    return [bucket['Name'] for bucket in response['Buckets']]
 
def is_bucket_existed(bucket_name):
    s3 = boto3.client('s3')
    response = s3.list_buckets()
    for bucket in response['Buckets']:
        if bucket['Name'] == bucket_name:
            return True
    return False

def upload_to_bucket(file_path):
    s3 = boto3.client('s3')
    if not is_bucket_existed(PROJECT_BUCKET):
        s3.create_bucket(Bucket=PROJECT_BUCKET,
                         CreateBucketConfiguration={'LocationConstraint': LOCATION_CONSTRAINT})

    file_name = get_file_name_from_uri(file_path)
    s3.upload_file(file_path, PROJECT_BUCKET, file_name)

def is_database_existed(cursor, database_name):
    query = "show databases"
    cursor.execute(query)
    for row in cursor.fetchall():
        if row['Database'] == database_name: # ['Tables_in_mysql']
            return True
    return False

def create_database(cursor, database_name):
    cursor.execute('create database ' + database_name)

def is_table_existed(cursor):
    with open('sqldb/check_table.sql', 'r') as file:
        return cursor.execute(file.read().replace('\n', '')) == 1 

def drop_table(cursor):
    with open('sqldb/drop_table.sql', 'r') as file:
        return cursor.execute(file.read().replace('\n', '')) == 1 

def create_table(cursor):
    with open('sqldb/create_table.sql', 'r') as file:
        cursor.execute(file.read().replace('\n', ''))

def get_insert_statement():
    with open('sqldb/insert_table.sql', 'r') as file:
        return file.read().replace('\n', '')

def add_full_data_set(result, cursor, fields):
    query_string = "select sr.* from sales_records sr"
    keys_list = list(fields.keys())
    len_of_keys = len(keys_list)
    if len_of_keys == 0:
        pass
    else:
        query_string = query_string + " where sr.{}='{}' ".format(keys_list[0], fields[keys_list[0]])
        for index in range(1, len_of_keys):
            query_string = query_string + "and sr.{}='{}' ".format(keys_list[index], fields[keys_list[index]])
    
    query_string = query_string + ";"
    cursor.execute(query_string)
    
    resultset = []
    rows = cursor.fetchall()
    for row in rows:
        row['order_id'] = int(row['order_id'])
        row['order_date'] = str(row['order_date'])
        row['ship_date'] = str(row['ship_date'])
        row['unit_sold'] = int(row['unit_sold'])
        row['unit_price'] = float(row['unit_price'])
        row['unit_cost'] = float(row['unit_cost'])
        row['total_revenue'] = float(row['total_revenue'])
        row['total_cost'] = float(row['total_cost'])
        row['total_profit'] = float(row['total_profit'])
        row['order_processing_time'] = int(row['order_processing_time'])
        row['gross_margin'] = float(row['gross_margin'])
        resultset.append(row)

    result['result_set'] = resultset

def add_data_aggregations(result, cursor, fields):
    with open('sqldb/query_data_agg.sql', 'r') as file:
        query_string = file.read().replace('\n', '')
    
    keys_list = list(fields.keys())
    len_of_keys = len(keys_list)
    if len_of_keys == 0:
        pass
    else:
        query_string = query_string + "where sr.{}='{}' ".format(keys_list[0], fields[keys_list[0]])
        for index in range(1, len_of_keys):
            query_string = query_string + "and sr.{}='{}' ".format(keys_list[index], fields[keys_list[index]])
    
    query_string = query_string + ";"
    cursor.execute(query_string)
    data = cursor.fetchall()

    result['avg_order_processing_time'] = data[0]['avg_order_processing_time']
    result['avg_gross_margin'] = data[0]['avg_gross_margin']
    result['avg_unit_sold'] = data[0]['avg_unit_sold']
    result['max_unit_sold'] = data[0]['max_unit_sold']
    result['min_unit_sold'] = data[0]['min_unit_sold']
    result['total_unit_sold'] = data[0]['total_unit_sold']
    result['total_total_revenue'] = data[0]['total_total_revenue']
    result['total_total_profit'] = data[0]['total_total_profit']
    result['total_orders'] = data[0]['total_orders']

def mysql_5_7_22_get_full_data_set(cursor, fields):
    with open('sqldb/query_full_data_set.sql', 'r') as file:
        query_string = file.read().replace('\n', '')
    
    keys_list = list(fields.keys())
    len_of_keys = len(keys_list)
    if len_of_keys == 0:
        pass
    else:
        query_string = query_string + "where sr.{}='{}' ".format(keys_list[0], fields[keys_list[0]])
        for index in range(1, len_of_keys):
            query_string = query_string + "and sr.{}='{}' ".format(keys_list[index], fields[keys_list[index]])
    
    query_string = query_string + ";"
    cursor.execute(query_string)
    return cursor.fetchall()[0]['full_data_set'] # take only first row



