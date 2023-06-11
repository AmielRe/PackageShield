package com.amiel.packageshield.controllers;

import com.amiel.packageshield.payload.request.ScanRequest;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1")
public class ScanController {

    @Value("${graphqlApiUrl}")
    private String graphqlApiUrl;

    GraphQlClient client;

    @PostConstruct
    void init() {
        WebClient wc = WebClient.create(graphqlApiUrl);
        client = HttpGraphQlClient.create(wc);
    }

    @PostMapping("/scan")
    public String signIn(@Valid @RequestBody ScanRequest scanRequest) {
        return "OK";
    }
}
