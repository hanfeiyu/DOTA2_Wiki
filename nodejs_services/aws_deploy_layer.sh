#!/bin/bash

# ----------------------
# author: Varik Hoang
# email : varikmp@uw.edu
# ----------------------

if [ -z "$1" ]
then
	echo "Usage: $0 PACKAGE_NAME"
	exit
fi

CURRENT_DIR=$(pwd)
PACKAGE_NAME=$1
LANGUAGE_VERSION=$(cat aws_deploy_config.json | jq '."lambda.runtime.version"' | tr -d '"')

# create a new temporary directory
mkdir -p env_$PACKAGE_NAME/nodejs

# navigate to the directory
cd env_$PACKAGE_NAME/nodejs

# install the package
npm init & npm install --save $PACKAGE_NAME

# compress the library directory [nodejs]
cd ..
zip -r9 $CURRENT_DIR/"$PACKAGE_NAME"_layer.zip nodejs
cd $CURRENT_DIR

# publish the lambda layer
#aws lambda publish-layer-version --cli-connect-timeout 0 --layer-name $PACKAGE_NAME --zip-file fileb://"$PACKAGE_NAME"_layer.zip --compatible-runtimes $LANGUAGE_VERSION > layers/"$PACKAGE_NAME"_layer.json

# clean up work
#rm -rf python
#rm -rf env_$PACKAGE_NAME
#rm "$PACKAGE_NAME"_layer.zip
