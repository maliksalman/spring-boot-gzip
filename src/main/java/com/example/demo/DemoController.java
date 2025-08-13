package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

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
        return generateResponse(smallContent);
    }

    @GetMapping("/large")
    public ResponseEntity<String> getLargeContent() throws IOException {
        return generateResponse(largeContent);
    }

    @GetMapping("/tiny")
    public ResponseEntity<String> getTinyContent() throws IOException {
        return generateResponse(tinyContent);

    }

    private ResponseEntity<String> generateResponse(Resource resource) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(resource.getContentAsString(Charset.defaultCharset()), headers, HttpStatus.OK);
    }

    @GetMapping("/client")
    public List<ClientResponse> doClientStuff(@Value("${server.port:8080}") int port) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        RestClient client = RestClient.builder().build();
        List<ClientResponse> responses = new ArrayList<>();

        for (String name : List.of("tiny", "small", "large")) {
            String uri = String.format("http://localhost:%d/%s", port, name);
            List<DataElement> data = client.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            responses.add(new ClientResponse(name,
                    mapper.writeValueAsString(data).length(),
                    data.size()));
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
        long dataSize,
        int dataElements
    ) {}
}
