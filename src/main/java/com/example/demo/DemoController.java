package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    private final Resource smallContent;
    private final Resource largeContent;
    private final Resource tinyContent;

    public DemoController(@Value("${data.small}") Resource smallContent,
                          @Value("${data.large}") Resource largeContent,
                          @Value("${data.tiny}") Resource tinyContent) {
        this.smallContent = smallContent;
        this.largeContent = largeContent;
        this.tinyContent = tinyContent;
    }

    @GetMapping("/small")
    public ResponseEntity<String> getSmallContent() throws IOException {
        return getResponse(smallContent);
    }

    @GetMapping("/large")
    public ResponseEntity<String> getLargeContent() throws IOException {
        return getResponse(largeContent);
    }

    @GetMapping("/tiny")
    public ResponseEntity<String> getTinyContent() throws IOException {
        return getResponse(tinyContent);

    }

    @GetMapping("/client")
    public Map<String, ClientResponse> doClientStuff() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String, ClientResponse> responses = new HashMap<>();
        RestClient client = RestClient.builder().build();
        for (String name : List.of("tiny", "small", "large")) {
            ResponseEntity<List<DataElement>> entity = client.get()
                    .uri("http://localhost:8080/" + name)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<DataElement>>() {});

            responses.put(name,
                    new ClientResponse(name,
                            mapper.writeValueAsString(entity.getBody()).length(),
                            entity.getBody().size()));
        }
        return responses;
    }

    record DataElement(
            int age,
            String eyeColor,
            String favoriteFruit
    ) { }

    record ClientResponse(
        String name,
        long contentLength,
        int elements
    ) {}

    private ResponseEntity<String> getResponse(Resource resource) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(resource.getContentAsString(Charset.defaultCharset()), headers, HttpStatus.OK);
    }
}
