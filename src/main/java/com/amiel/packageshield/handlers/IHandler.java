package com.amiel.packageshield.handlers;

import com.amiel.packageshield.payload.request.ScanRequest;
import com.amiel.packageshield.payload.response.IResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface IHandler {
    ResponseEntity<IResponse> handle(@Valid final ScanRequest scanRequest);
}