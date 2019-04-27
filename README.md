# Auth1

View the API [here](api.md).

This project contains 4 directories:
#### auth1/
In this directory, you will find the source code for the Auth1 service. The Auth1 service is built with the [Spring Boot framework](https://spring.io/projects/spring-boot). You can run the service by using Maven to create the .jar file and either manually run the .jar file (assumes MySQL and Redis are running) or use the docker-compose.yml to run Auth1 with MySQL and Redis.
#### auth1api/
In this directory, you will find the source code for the Auth1 Python client library. This library wraps the Auth1 API and is also publically available on [PyPi](https://pypi.org/project/auth1api/)
#### auth1example/
In this directory, you will find the source code for an example project that uses the Auth1 service for its authentication. This example project is written in Python Flask and uses the auth1api client library. Additionally, this example project showcases login, register, and two factor authentication
#### auth1release/
In this directory, you will find all the files needed to run auth1. Specifically, there is a docker-compose.yml along with 3 configuration files. With `docker-compose up` you can start up and instance of Auth1 (along with its dependencies) with the settings specified in the configuration files.

## Usage
In auth1release/, you can run an instance of Auth1 with `docker-compose up`. You can toggle the Auth1 configurations in the files with the .cnf suffix
