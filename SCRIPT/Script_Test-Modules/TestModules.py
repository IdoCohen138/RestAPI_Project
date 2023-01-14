import os
import shutil

path_to_your_location="C:/Users/Ido/PycharmProjects/SCRIPT/Script_Test-Modules"

# Clone the repository
os.system("git clone https://github.com/IdoCohen138/RestAPI_Project.git")

# Navigate to the persistence module
os.chdir(path_to_your_location+"/RestAPI_Project")

# Copy the config.properties file to persistence module
shutil.copy2(path_to_your_location + "/config.properties", "persistence/src/test/resources/")

os.mkdir(path_to_your_location + "/RestAPI_Project/job/src/test/resources")

# Copy the config.properties file to job module
shutil.copy2(path_to_your_location + "/config.properties", "job/src/test/resources/")

# Copy the .env file
shutil.copy2(path_to_your_location + "/.env", path_to_your_location+"/RestAPI_Project")

# Package the module using Maven
os.system("mvn package -f job_pom.xml")
