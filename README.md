# RestAPI_Project

## Explain how to run the tests:
In the root directory RestAPI_Project you can found SCRIPT folder, inside you can found two test folder that contains the scripts:
"Script_Test-E2E" and "Script_Test-Modules".

Requerment for tests:
- docker.
- postgres account.

Requerment for script:
- python.
- do not clone the repository, the script do it for you.  
- download two folders "Script_Test-E2E" and "Script_Test-Modules" to your pc.  

 > **to download specific folder from github repository you can use this tool: https://download-directory.github.io/**  
 > copy this url for Script_Test-E2E and paste in tool:  
 > ```
 > https://github.com/IdoCohen138/RestAPI_Project/tree/main/SCRIPT/Script_Test-E2E
 > ```  
 > copy this url for Script_Test-Modules and paste in tool:  
 > ```
 > https://github.com/IdoCohen138/RestAPI_Project/tree/main/SCRIPT/Script_Test-Modules
 > ```

- change the variable inside each script named "path_to_your_location" to your script path location in your local pc.  

> for example in "TestE2E.py" file:
> ```
> path_to_your_location="C:/Users/Ido/PycharmProjects/SCRIPT/Script_Test-Modules"  
> ```
>  or in "TestModules.py" file:
> ```
> path_to_your_location="C:/Users/Ido/PycharmProjects/SCRIPT/Script_Test-E2E"
> ```
### EndToEnd Test:
run the "TestE2E.py" script from "Script_Test-E2E" folder after you read instractions below  
Requierments for this script:
1. set a "config.properties" file
2. set a ".env" file for E2E Test  
this files need to be in the same location path that the TestE2E.py file location.  
3. run TestE2E.py

### Modules Test:  
run the "TestModules.py" script from "Script_Test-Modules" folder after you read instractions below  
Requierments for this script:
1. run database container:  
**docker run -p 5433:5432 -e POSTGRES_PASSWORD=YOUR_PASSWORD_TO_POSTGRES -d database_container**
2. set a "config.properties" file
3. set a ".env" file for Modules Test  
this files need to be in the same location path that the TestModules.py file location.  
4. run TestE2E.py

#### config.properties file:   
(for Modules Test you need only the webhook properties without "docker_compose_path propertie)
```
webhook_message_api=WEBHOOK FOR "api-messages" CHANNEL(example: https://hooks.slack.com/services/YYYYYYYYYYYYYYYYYYYYYYYYYYY/YYYYYYYYYYYYYYYYYY)
webhook_message_api_2=WEBHOOK FOR "api-messages2" CHANNEL(example: https://hooks.slack.com/services/XXXXXXXXXXXXXXXXXXXXXXXXXX/XXXXXXXXXXXXXXXXXX)
docker_compose_path=PATH_TO_YOUR_"RestAPI-Project"_FOLDER(example: C:\\Users\\Ido\\PycharmProjects\\SCRIPT\\Script_Test-E2E\\RestAPI_Project)
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
