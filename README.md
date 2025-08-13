# spring-boot-basics

This repository tracks the progress of our KT sessions, as we introduce more and more spring-framework and spring-boot features. The main areas of focus in the order they were introduced are:

- MVC controllers
- Inversion of Control (IOC)
- Dependency Injection
- Externalized Configuration
- Spring Profiles
- JDBC repositories
- Making REST calls

## Building

```
./mvnw clean package
```

## Running

```
java -jar target/demo*.jar
```

If running with a specific profile, for example `db`, then run:

```
java -jar target/demo*.jar --spring.profiles.active=db
```

To access the `heroes` endpoint using `curl` and `jq`, run:

```
curl -s localhost:8080/heroes | jq .
```

To access the `heroes` endpoint using `http` command (from [httpie](https://httpie.io/cli) project), run:
```
http localhost:8080/heroes
```

## Postgres SQL

If running with Postgres DB, you will need to either start a DB instance in docker or modify the configuration values specified in [src/main/resources/application-db.yml](src/main/resources/application-db.yml). To start a postgres instance running in docker, run:

```
docker run -d --rm \
    --name postgres \
    -e POSTGRES_PASSWORD=secret \
    -p 5432:5432 \
    postgres:15
```

To run `psql` commands against the DB (like `create table ...`, `delete from ...`, or any other SQL), run:

```
docker exec -it postgres \
    psql -U postgres -d postgres
```

To terminate the container and all the data in it, run:

```
docker rm -f postgres
```

## Running Local *httpbin*

```
docker run -d --rm \
    --name httpbin \
    -p 8585:80 \
    kennethreitz/httpbin
```
# spring-boot-gzip
# spring-boot-gzip
