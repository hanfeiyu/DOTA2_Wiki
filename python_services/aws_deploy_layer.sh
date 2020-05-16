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
PYTHON_EXEC_PATH=$(which python)
LANGUAGE_VERSION=$(cat aws_deploy_config.json | jq '."lambda.runtime.version"' | tr -d '"')

# create a new virtual environment
virtualenv -p $PYTHON_EXEC_PATH env_$PACKAGE_NAME

# activate the environment just created
source ./env_$PACKAGE_NAME/bin/activate

# install the package
pip install $PACKAGE_NAME

# remove all cached files
find . | grep -E "(*.dist-info|__pycache__|\.pyc|\.pyo$)" | xargs rm -rf

# exit the current environment
deactivate

# move the library directory [site-packages]
# to the workspace and rename to [python] (very important for lambda function to recognize)
mv env_$PACKAGE_NAME/lib/$LANGUAGE_VERSION/site-packages $CURRENT_DIR/python

# compress the library directory [python]
zip -r9 $CURRENT_DIR/"$PACKAGE_NAME"_layer.zip python

# publish the lambda layer
aws lambda publish-layer-version --cli-connect-timeout 0 --layer-name $PACKAGE_NAME --zip-file fileb://"$PACKAGE_NAME"_layer.zip --compatible-runtimes $LANGUAGE_VERSION > layers/"$PACKAGE_NAME"_layer.json

# clean up work
rm -rf python
rm -rf env_$PACKAGE_NAME
rm "$PACKAGE_NAME"_layer.zip