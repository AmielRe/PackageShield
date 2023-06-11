package com.amiel.packageshield.handlers;

import com.amiel.packageshield.payload.request.ScanRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("npm")
public class NpmHandler implements Handler {
    @Override
    public ResponseEntity<String> handle(@Valid ScanRequest scanRequest) {
        // Logic for npm handler
        return ResponseEntity.ok("NpmHandler executed");
    }
}
