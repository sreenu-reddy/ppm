[![Build Status](https://travis-ci.com/sreenu-reddy/ppm.svg?branch=master)](https://travis-ci.com/sreenu-reddy/ppm)  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=alert_status)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Coverage Status](https://coveralls.io/repos/github/sreenu-reddy/ppm/badge.svg?branch=master)](https://coveralls.io/github/sreenu-reddy/ppm?branch=master)  [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=bugs)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=code_smells)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=coverage)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=security_rating)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=sreenu-reddy_ppm&metric=sqale_index)](https://sonarcloud.io/dashboard?id=sreenu-reddy_ppm)

The main aim of the project to create a project that driven by tests by using cutting-edge technologies and following principles of software engineering. The API that I have created is called “Personal Project Management Tool” for managing the his/her personal projects. To develop this project, have created a CI/CD pipeline with IntellijIDE,GIT and TravisCI and used Sonar Cloud, Coveralls for code analysis, code coverage respectively.
I have developed it while following TestDrivenDevelopment(TDD) methodology and SOLID principles of Java.



# PersonalProjectManagementTool-is a WepApplication with the following technologies:
- Java 11
- SpringBoot
- MySql
- H2 database
- junit5
- Mockito
- Git
- Coveralls
- SonarCloud
- TravisCi
- Postman
- GitHub
- Maven

# Methodologies
- TDD
- BDD

if you want to use your MySQL server on docker then use the following instructions:
1: get mysql image from docker hub using :docker pull mysql/mysql-server:latest
2: connect to it from any of your sqlWorkBench using: docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql
if you need more details on how to customize the commands look-up in docker Hub page of mysql.


With Maven:
run the following command in a terminal window (in the complete) directory: ./mvnw spring-boot:run
command to build: mvn clean install 



