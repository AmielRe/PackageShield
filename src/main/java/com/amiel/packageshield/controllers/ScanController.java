package com.amiel.packageshield.controllers;

import com.amiel.packageshield.handlers.Handler;
import com.amiel.packageshield.payload.request.ScanRequest;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1")
public class ScanController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Value("${graphqlApiUrl}")
    private String graphqlApiUrl;

    GraphQlClient client;

    @PostConstruct
    void init() {
        WebClient wc = WebClient.create(graphqlApiUrl);
        client = HttpGraphQlClient.create(wc);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> signIn(@Valid @RequestBody ScanRequest scanRequest) {
        try {
            // Get appropriate handler according to ecosystem (handle case-sensitive)
            Handler handler = getContext().getBean(scanRequest.getEcosystem().toLowerCase(), Handler.class);
            return handler.handle(scanRequest);
        } catch(Exception ignored) {
            return ResponseEntity.badRequest().body(String.format("Invalid param value - '%s' ecosystem is not supported currently", scanRequest.getEcosystem()));
        }
    }
}
