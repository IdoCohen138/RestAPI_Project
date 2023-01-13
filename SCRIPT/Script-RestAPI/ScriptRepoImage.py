
import subprocess

# Build the Docker image
subprocess.run(["docker", "build", "-t", "idoco138/rest_api-project", "."])

# Log in to Docker Hub
subprocess.run(["docker", "login", "-u", "idoco138", "-p", "dckr_pat_pz2VfvpK484h9FrWEBE1vDc5yUA"])

# Push the image to Docker Hub
subprocess.run(["docker", "push", "idoco138/rest_api-project"])

