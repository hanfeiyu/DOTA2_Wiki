#!/bin/bash

# ----------------------
# author: Varik Hoang
# email : varikmp@uw.edu
# ----------------------

FUNCTION_NAME=$(cat aws_deploy_config.json | jq '."lambda.function.name"' | tr -d '"')
REQUEST_DATA=$(cat aws_deploy_config.json | jq -c '."sample.request.data"')

aws lambda invoke --invocation-type RequestResponse --cli-read-timeout 900 --function-name $FUNCTION_NAME --payload $REQUEST_DATA /dev/stdout