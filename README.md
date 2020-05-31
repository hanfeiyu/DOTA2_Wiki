# DOTA2_Wiki

DOTA2_Wiki is a full-stack application which help users search the information of DOTA2 heros, items, abilities and famous players, as well as recommend potential suitable Heros based on users' interests.
DOTA2 is an Action RTS game, developed by Valve Corporation.
All our data is gathered from real DOTA2 data from the DOTA2 game client and official website.

## Architecture

DOTA2_Wiki is a full-stack application with server based frontend and
serverless backend.

### Environment

DOTA2_Wiki was developed on MacOS, using Eclipse IDE.
Dependencies includes Java 11+, PHP 7.3+, Maven 3.6+.

### Frontend

#### Introduction
DOTA2_Wiki frontend is developed using HTML/CSS and PHP/Bootstrap. Source code
of frontend can be found in [frontend/src](./frontend/src).

To make sure PHP files correctly parsed by Apache server, you'll have to check
if your Apache server is running, then either reset your Apache server root path
to somewhere else or copy frontend folder to your Apache root,
`/Library/WebServer/Documents` for MacOS.

#### Configuration
For MacOS, simply (re)start Apache server:

```
sudo apachectl restart
```

Then copy frontend folder to Apache root path:

```
sudo cp -r [your local DOTA2_Wiki]/frontend /Library/WebServer/Documents
```

Check if your Apache server working or not by enter the following link into
your Browser (Chrome preferred):

```
localhost/frontend/index.html
```

If seeing the background, then your frontend configuration is good to go.

### Backend

#### Introduction
DOTA2_Wiki backend is developed using Java 11, Maven 3.6.3. The backend is completely serverless deployed on AWS with multiple
AWS services.

Before deploy DOTA2_Wiki, you may want to familiarize with following AWS
services:
[AWS EC2](https://aws.amazon.com/ec2/)
[AWS Lambda](https://aws.amazon.com/lambda/)
[AWS API Gateway](https://aws.amazon.com/api-gateway/)
[AWS RDS](https://aws.amazon.com/rds/)

Except for AWS services, DOTA2_Wiki uses [SAAF](https://github.com/wlloyduw/SAAF) as development/deployment framework.
You may also want to familiarize with SAAF as well.

#### Configuration
Once all the things mentioned above are no longer a problem, you may start backend deployment.

##### RDS
Fist set up an Aurora serverless RDS instance and take care of your RDS password.
Notice: Type of the RDS instance must be specified as `Aurora -> Serverless -> MySql`.

##### EC2
After RDS instance created, you can boot up a EC2 instance with any type (free
tier: T2.micro) to create and populate data into RDS database:

```
ssh -i [your pem file] ubuntu@[your EC2 instance IP]

sudo apt update && sudo apt install -y mysql-server mysql-client libmysqlclient-dev
```

Then go on: 

```
git clone https://github.com/hanfeiyu/DOTA2_Wiki
cd DOTA2_Wiki
cat Dota2Wiki.sql
```

Copy and paste all the Sql codes into RDS instance. Login your RDS instance using the password you
created for RDS instance previously:

```
mysql -h [your RDS endpoint] -P 3306 -u [your RDS username] -p
```

##### Lambda
Let's start with building the generic lambda function package:

```
cd [your local DOTA2_Wiki]/backend
mvn clean -f pom.xml
mvn verify -f pom.xml
```

Then go to your AWS account and create the following ten lambda function APIs:
```
DOTA2_Wiki_GetItem	 
DOTA2_Wiki_GetAbility
DOTA2_Wiki_DropCache
DOTA2_Wiki_GetView	 
DOTA2_Wiki_DeleteCache
DOTA2_Wiki_GetPlayer
DOTA2_Wiki_GetRec	 
DOTA2_Wiki_GetCache	 
DOTA2_Wiki_PutCache	 
DOTA2_Wiki_GetHero
```

For each function: 
1. MUST set functions in the same VPC and sub-net(s) with your RDS instance.
2. MUST Add four environment variables for each function:

| **Key** | **Value** |
| --------- | --------------- |
| DB_ENDPOINT | [your RDS endpoint] |
| DB_NAME | Dota2wiki |
| DB_PASSWORD | [your RDS password] |
| DB_USERNAME | [your RDS username] |

3. Set Timeout inside Basic settings to 1+ min.
4. MUST upload `lambda_test-1.0-SNAPSHOT.jar` under `backend/target` and click
   `Save`.

You may test each function individually by creating a simple test JSON using
`Test` button, to ensure that all of them work correctly.

#### API Gateway
Similar to creating Lambda functions, each funtion will need an API Gateway to
route invocations from anywhere in the world outside VPC.
You will need to create ten corresponding API Gateways:

```
DOTA2_Wiki_DeleteCache	
DOTA2_Wiki_DropCache	
DOTA2_Wiki_GetAbility	
DOTA2_Wiki_GetCache		
DOTA2_Wiki_GetHero		
DOTA2_Wiki_GetItem		
DOTA2_Wiki_GetPlayer	
DOTA2_Wiki_GetRec		
DOTA2_Wiki_GetView		
DOTA2_Wiki_PutCache		
```

Each API Gateway shall:
1. Select REST API as its API type.
2. Select POST as its API method. 
3. Have its unique corresponding Lambda function linked.
4. Save and deploy API.

After all API Gateways deployed, switch to [worker.php](./frontend/src/worker.php) and find function `curlLambda($api)`.
Replace all the `$apigateway` to the API Gateway that each funtion linked to.

Okay! Now the backend deployment should be done!
Enjoy your journey with DOTA2 world!

### Reference

[DOTA_2_Wiki](https://dota2.gamepedia.com/Dota_2_Wiki)
[liquipedia](https://liquipedia.net/dota2/Main_Page)
[SAAF](https://github.com/wlloyduw/SAAF)

### Project Github Link

[DOTA2_Wiki](https://github.com/hanfeiyu/DOTA2_Wiki)

### @Developers

[Hanfei Yu](https://github.com/hanfeiyu)
[Tong Wu](https://github.com/WhoenyWu)
[Enbei Liu](https://github.com/XBrOtk)







































































































































