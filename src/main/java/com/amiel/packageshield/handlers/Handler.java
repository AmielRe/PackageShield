package com.amiel.packageshield.handlers;

import com.amiel.packageshield.payload.request.ScanRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface Handler {
    ResponseEntity<String> handle(@Valid ScanRequest scanRequest);
}