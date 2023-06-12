package com.amiel.packageshield.handlers;

import com.amiel.packageshield.models.Dependency;
import com.amiel.packageshield.models.VulnerableDependency;
import com.amiel.packageshield.payload.request.ScanRequest;
import com.amiel.packageshield.payload.response.IResponse;
import com.amiel.packageshield.services.VulnerabilityScanner;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public abstract class BaseHandler implements Handler{

    @Autowired
    List<VulnerabilityScanner> vulnerabilityScanners;

    @Override
    public ResponseEntity<IResponse> handle(@Valid final ScanRequest scanRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(scanRequest.getFileContent());
        String decodedFileContent = new String(decodedBytes);

        // Custom logic implemented by subclasses
        return customHandleLogic(decodedFileContent, scanRequest.getEcosystem());
    }

    protected List<VulnerableDependency> scanWithAllScanners(final Dependency packageToScan, final String ecoSystem) {
        List<VulnerableDependency> vulnerableDependencies = new ArrayList<>();
        for (VulnerabilityScanner vulnerabilityScanner : vulnerabilityScanners) {
            VulnerableDependency vulnerableDependency = vulnerabilityScanner.checkDependencyForVulnerability(packageToScan, ecoSystem);

            if(vulnerableDependency != null) {
                vulnerableDependencies.add(vulnerableDependency);
            }
        }

        return vulnerableDependencies;
    }

    protected abstract ResponseEntity<IResponse> customHandleLogic(final String decodedFileContent, final String ecoSystem);
}
