# spring-boot-gzip

A sample spring-boot app that demonstrates usage of gzip when serving/consuming HTTP content.

The following endpoints demo serving of gzip'ed content (if the content is larger than 1KB):

- `/tiny`
- `/small`
- `/large`

The `/tiny` content will not be compressed, the other two will be if `Accept-Encoding: gzip` HTTP header is part of the request.   

The `/client` endpoint acts as the HTTP client of its own 3 endpoints and reports information about the response. Check the application log for wire-level information about these requests as they are processed using `org.apache.hc.client5` libraries through the `org.springframework.web.client.RestClient` abstraction.


## Building

```
./mvnw clean package
```

## Running

```
java -jar target/spring-boot-gzip-1.0.jar
```
