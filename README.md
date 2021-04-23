[![Build Status](https://travis-ci.org/Relesi/modernization-biometric.svg?branch=master)](https://travis-ci.org/Relesi/modernization-biometric)

## Biometric Modernization
Biometric system API with Java and Spring Boot.
### RESTful API Details
The RESTful Modernization Biometric API contains the following features:
* Project created with Spring Boot and Java 8
* MySQL database with JPA and Spring Data JPA
* Authentication and authorization with Spring Security and JWT (JSON Web Token) tokens
* Database migration with Flyway
* Unit tests and integration with JUnit and Mockito
* Caching with EhCache
* Continuous integration with TravisCI
### How to run the application
Be sure to have Maven installed and added to the PATH of your operating system, just like Git.

git clone https://github.com/Relesi/modernization-biometric
cd modernization-biometric-api
mvn spring-boot: run
Access the endpoints through the http://localhost: 8088 url

### Importing the project into Eclipse or STS

At the terminal, perform the following operation:

    mvn eclipse: eclipse

In Eclipse, STS or Intellij, import the project as a Maven project.
