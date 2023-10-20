/*package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class Restservice {
    private final RestTemplate woodiarest;

    @Autowired
    public Restservice(RestTemplate woodiarest) {
        this.woodiarest = woodiarest;
    }
    public String  getdatawoosida() {
        String url = "https://api.wossidia.de/graph/290000003/%3Cedge%3Eam_transcription%3Cedge%3Enode";

        ResponseEntity<String> response = woodiarest.getForEntity(url, String.class);
        return response.getBody();

    }
}*/
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RestService {


    public String getdatawoosida() {

            return "Error occurred during API request.";

    }
}

