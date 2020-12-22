# Calculation Server 
A Spring server Rest API to send calculations too which are then calculated and saved.

# Usage
The server needs a mysql database to function. A Docker-compose file is available to start one.
To start the docker-compose file from the calculation-server use:
```
docker-compose -f docker/docker-compose.yml up -d
```

To run the server for development: 
```
mvn spring-boot:run
```

To run the tests use:
```
mvn test
```

To build deployable jar:
```
mvn package
```
The jar is located in the target folder


