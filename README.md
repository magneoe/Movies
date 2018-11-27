# Movies


## Overview & Installation
The project is written with Java jdk8, using the Spring Boot project.
Frontend is setup using JSP at the master branch. The rest-api branch is utilizing spring rest controllers with ReactJS in the MovieDB frontend  project (https://github.com/magneoe/Movies-frontend). 

Setup when using rest-api branch:

1) Make sure Maven dependencies are being downloaded and the project is built locally.
2) From a IDE (e.g. Eclipse), run no.itminds.movies.App as a regular Java application with the developer profile (with vm argument -Dspring.profiles.active=dev). Prod profile is configured for AWS.
3) This boots up a tomcat server at port 8082, using a preconfigured H2 database at project-root/movies.mv.db. 




