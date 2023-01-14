# RestAPI_Project

## Explain how to run the tests:
In the root directory RestAPI_Project you can found SCRIPT folder, inside you can found two test folder that contains the scripts:
"Script_Test-E2E" and "Script_Test-Modules".

### EndToEnd Test:
you need to download "TestE2E.py" script from "Script_Test-E2E" folder.(dont clone the repository, the script do it for you)  
Requierments for this script:
1. set a "config.properties" file
2. set a ".env" file for E2E Test  
this files need to be in the same location path that the TestE2E.py file location.

### Modules Test:
you need to download "TestModules.py" script from "Script_Test-Modules" folder.(dont clone the repository, the script do it for you)
Requierments for this script:
1. set a "config.properties" file
2. set a ".env" file for Modules Test  
this files need to be in the same location path that the TestModules.py file location.


#### config.properties file:
```
webhook_message_api=WEBHOOK FOR "api-messages" channel
webhook_message_api_2=WEBHOOK FOR "api-messages2" channel
docker_compose_path=PATH_TO_YOUR_"RestAPI-Project"_FOLDER
```
#### .env file for E2E Test:
```
POSTGRES_USER=YOUR_USER
POSTGRES_PASSWORD=YOUR_PASSWORD
POSTGRES_DB=postgres
POSTGRES_HOST=postgres_database
POSTGRES_PORT=5432
SERVER_PORT_JOB=8081
SERVER_PORT_RESTAPI=8080
COMPOSE_FILE=docker-compose.yaml
```
#### .env file for Modules Test:
```
POSTGRES_USER=YOUR_USER
POSTGRES_PASSWORD=YOUR_PASSWORD
POSTGRES_DB=postgres
POSTGRES_HOST=localhost
POSTGRES_PORT=5433
SERVER_PORT_JOB=8081
SERVER_PORT_RESTAPI=8080
COMPOSE_FILE=docker-compose.yaml
```


## Docker Compose

configure `.env` file :
``` 
# API 

# POSTGRES
POSTGRES_USER=username
POSTGRES_PASSWORD=password
POSTGRES_DB=channels
POSTGRES_HOST=postgres
POSTGRES_PORT=5432

# PGADMIN

```

cd to the [root dir](.) run the command: 
```
docker-compose up -d --build
```

You should have now a network with the containers:

1. [api](http://localhost:8080/)
2. [postgres](http://localhost:5432/)
3. [pgadmin](http://localhost:80/)


