#!/bin/bash

# ----------------------
# author: Varik Hoang
# email : varikmp@uw.edu
# ----------------------

if [ -z "$1" ] || [ -z "$1" ]
then
	echo "Usage : $0 PROJECT_PATH MEMORY_SETTING"
	echo "Sample: $0 lambda/ 512"
	exit
fi

CURRENT_PATH=$(pwd)
PROJECT_PATH=$1
MEMORY_SETTING=$2
LANGUAGE_VERSION=$(cat aws_deploy_config.json | jq '."lambda.runtime.version"' | tr -d '"')

# get the function name from the aws_deploy_config.json file
FUNCTION_NAME=$(cat aws_deploy_config.json | jq '."lambda.function.name"' | tr -d '"')
FUNCTION_HANDLER=$(cat aws_deploy_config.json | jq '."lambda.function.handler"' | tr -d '"')
ROLE_ARN=$(cat aws_deploy_config.json | jq '."lambda.role.arn"' | tr -d '"')
SUBNETS=$(cat aws_deploy_config.json | jq '."lambda.subnets"' | tr -d '"')
SECURITY_GROUPS=$(cat aws_deploy_config.json | jq '."lambda.security.groups"' | tr -d '"')

# compress all source files
cd $PROJECT_PATH
zip -X -r $CURRENT_PATH/index.zip *
cd $CURRENT_PATH

aws lambda create-function --function-name $FUNCTION_NAME --runtime $LANGUAGE_VERSION --role $ROLE_ARN --timeout 900 --handler $FUNCTION_HANDLER --zip-file fileb://index.zip
aws lambda update-function-code --function-name $FUNCTION_NAME --zip-file fileb://index.zip
aws lambda update-function-configuration --function-name $FUNCTION_NAME --memory-size $MEMORY_SETTING --runtime $LANGUAGE_VERSION --vpc-config SubnetIds=[$SUBNETS],SecurityGroupIds=[$SECURITY_GROUPS]

for file in layers/*_layer.json
do
	LAYER_ARN+="$(cat $file | jq '.LayerVersionArn' | tr -d '"') "
done

# connect the lambda function to the lambda layer
aws lambda update-function-configuration --function-name $FUNCTION_NAME --layers $LAYER_ARN

rm index.zip