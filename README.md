# A Country application - backend service
- for getting all countries
- for getting country details by country name

This is a Spring boot application!

## Description and Configuration
The following [web-service](https://restcountries.com/v2}) is used for sourcing of country information
This country information is cached by default for 600 minutes. 
It means that information could be stale for at most this period.

## Building the project
This project uses Java 17 and Maven. You can use a maven wrapper from command line with ./mvnw command to build jar file

## Running the project
This application could be started without any additional configuration. The application will be running on port 9010
The following environment variables could be set to redefine default application properties
(When run locally in Intellij you can set environment variables on the spring boot run configuration tab)

```
 APP_PORT                          [port where this application will be running]
 COUNTRY_BASE_URL                  [Country information base URL. For example https://restcountries.com/v2]
 COUNTRY_CACHE_DURATION_IN_MINUTES [Country information cache duration]
```

## Testing and Health monitoring
- Swagger available by path ```/swagger-ui/``` for manual endpoint testing   
- Actuator available by path ```/actuator```
- Health check endpoint ```/actuator/health```
- Application info endpoint ```/actuator/info```

