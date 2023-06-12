package com.amiel.packageshield.handlers;

import com.amiel.packageshield.models.Dependency;
import com.amiel.packageshield.models.VulnerableDependency;
import com.amiel.packageshield.payload.request.ScanRequest;
import com.amiel.packageshield.payload.response.IResponse;
import com.amiel.packageshield.services.IVulnerabilityScanner;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public abstract class BaseHandler implements IHandler {

    @Autowired
    List<IVulnerabilityScanner> vulnerabilityScanners;

    /**
     * This function represents the handling of the ScanRequest - first decode file content
     * and then pass to custom logic handling
     * @param scanRequest The scan request received
     * @return Response - either a message (probably error) or ScanResponse with all vulnerabilities
     */
    @Override
    public ResponseEntity<IResponse> handle(@Valid final ScanRequest scanRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(scanRequest.getFileContent());
        String decodedFileContent = new String(decodedBytes);

        // Custom logic implemented by subclasses
        return customHandleLogic(decodedFileContent, scanRequest.getEcosystem());
    }

    /**
     * Iterate over each scanner and scan received dependency with received ecosystem
     * @param dependencyToScan The dependency we need to scan
     * @param ecoSystem The ecosystem for this dependency
     * @return List of all vulnerable dependencies found in scanners
     */
    protected List<VulnerableDependency> scanWithAllScanners(final Dependency dependencyToScan, final String ecoSystem) {
        List<VulnerableDependency> vulnerableDependencies = new ArrayList<>();
        for (IVulnerabilityScanner vulnerabilityScanner : vulnerabilityScanners) {
            VulnerableDependency vulnerableDependency = vulnerabilityScanner.checkDependencyForVulnerability(dependencyToScan, ecoSystem);

            if(vulnerableDependency != null) {
                vulnerableDependencies.add(vulnerableDependency);
            }
        }

        return vulnerableDependencies;
    }

    protected abstract ResponseEntity<IResponse> customHandleLogic(final String decodedFileContent, final String ecoSystem);
}
