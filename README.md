# Welcome to Users MVC

This repository contains the Users MVC application made on top of Users app domain module - [Users App Core Module](https://github.com/drigomedeiros/users-core). The intention is to use this repo to show how to build Jakarta EE MVC application in embedded servers, disposed to users as Single Page Application with minimal Javascript requirements.

## The Users App

This is a simple web application that does one only action: provide a list of registered users in a repository. For get this awesome functionality, the user must be authenticated and authorized to access the users list.

## How to see it working

As modern Java applications do nowadays, you can run the app with gradle by typing **./gradlew clean build** on the repository root. After, access the application on browser: [Users MVC Application](http://localhost:8080/app/). By this moment, the super secure admin username and password are "1" and "1" respectively.

## Infrastructure

The project, when concluded, will have:

- Jetty Embedded Servlet Container
- Jersey as Jakarta MVC implementation
- Mustache as template engine
- AdminLTE as frontend engine
- Hibernate as persistence provider (Under development)
- Keycloak to secure http requests (Under development)

## Related git repositories

- [Users App Core Module](https://github.com/drigomedeiros/users-core)
- [Jakarta EE Backend Project](#) - Future
- [Jakarta MVC Project (Only Frontend)](#) - Future
- [Springboot MVC Frontend Project](#) - Future
- [Springboot Backend Project](#) - Future
- [React Frontend Project](#) - Future
