import handler
import json

#
# AWS Lambda Functions Default Function
#
# This hander is used as a bridge to call the platform neutral
# version in handler.py. This script is put into the scr directory
# when using publish.sh.
#
# @param request
#
def extracting_handler(event, context):
	return handler.extract(event, context)

def loading_handler(event, context):
	return handler.load(event, context)

def querying_handler(event, context):
	return handler.query(event, context)