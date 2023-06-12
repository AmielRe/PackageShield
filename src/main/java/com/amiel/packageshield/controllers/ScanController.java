package com.amiel.packageshield.controllers;

import com.amiel.packageshield.handlers.Handler;
import com.amiel.packageshield.payload.request.ScanRequest;
import com.amiel.packageshield.payload.response.IResponse;
import com.amiel.packageshield.payload.response.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ScanController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    /**
     * Scan given dependencies for vulnerabilities.
     * @param scanRequest Request containing the "ecosystem" and the file content
     * @return Response - either a message (probably error) or ScanResponse with all vulnerabilities
     */
    @PostMapping("/scan")
    public ResponseEntity<IResponse> scan(@Valid @RequestBody final ScanRequest scanRequest) {
        try {
            // Get appropriate handler according to ecosystem (handle case-sensitive)
            Handler handler = getContext().getBean(scanRequest.getEcosystem().toLowerCase(), Handler.class);
            return handler.handle(scanRequest);
        } catch(BeansException ignored) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.format("Invalid param value - '%s' ecosystem is not supported currently", scanRequest.getEcosystem())));
        } catch(Exception ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}
