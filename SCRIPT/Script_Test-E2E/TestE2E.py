import os
import shutil

path_to_your_location="C:/Users/Ido/PycharmProjects/SCRIPT/Script_Test-E2E"

# Clone the repository
os.system("git clone https://github.com/IdoCohen138/RestAPI_Project.git")



# Navigate to the business module
os.chdir(path_to_your_location+"/RestAPI_Project/business")

# Copy the config.properties file
shutil.copy2(path_to_your_location + "/config.properties", "src/test/resources/")

# Copy the config.properties file
shutil.copy2(path_to_your_location + "/.env", path_to_your_location+"/RestAPI_Project")

# Package the module using Maven
os.system("mvn package")
