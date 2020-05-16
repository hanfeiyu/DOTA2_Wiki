'''
Created on Nov 24, 2019

@author: varikmp
'''

def error_key_not_found(inspector):
    inspector.addAttribute("response", "failed")
    inspector.addAttribute("error", "Could not find the key csv_file_url in the request")

def error_file_not_found(inspector, file_name):
    inspector.addAttribute("response", "failed")
    inspector.addAttribute("error", "Could not find the CSV file " + file_name + " in either temporary directory and S3")

def error_mysql(inspector, message):
    inspector.addAttribute("response", "failed")
    inspector.addAttribute("error", message)
    