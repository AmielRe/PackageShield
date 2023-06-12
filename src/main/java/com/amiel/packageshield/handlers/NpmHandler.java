package com.amiel.packageshield.handlers;

import com.amiel.packageshield.models.Dependency;
import com.amiel.packageshield.payload.response.IResponse;
import com.amiel.packageshield.payload.response.ScanResponse;
import com.amiel.packageshield.models.VulnerableDependency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("npm")
public class NpmHandler extends BaseHandler {

    private final String PACKAGE_JSON_DEPENDENCIES_ROOT = "dependencies";

    private static final Logger logger = LoggerFactory.getLogger(NpmHandler.class);

    /**
     * Get file content and ecosystem, parse it as package.json file and scan each dependency for
     * vulnerabilities using all scanners
     * @param decodedFileContent File content param after base64 decode
     * @param ecoSystem Ecosystem param (
     * @return IResponse object containing either the ScanResponse with the VulnerableDependencies or MessageResponse
     * With error
     */
    @Override
    protected ResponseEntity<IResponse> customHandleLogic(final String decodedFileContent, final String ecoSystem) {
        ScanResponse response = new ScanResponse(new ArrayList<>());
        List<Dependency> dependenciesToScan = parseFileContent(decodedFileContent);
        logger.info(String.format("Found %d dependencies to scan, starting scan now", dependenciesToScan.size()));
        for (Dependency dep : dependenciesToScan) {
            List<VulnerableDependency> vulnerableDependencies = super.scanWithAllScanners(dep, ecoSystem);

            // Iterate over all vulnerable dependencies found and only add new ones.
            for (VulnerableDependency currVulDep : vulnerableDependencies) {
                if(response.getVulnerableDependencies().stream().noneMatch(vulnerableDependency -> vulnerableDependency.getName().equalsIgnoreCase(currVulDep.getName()))){
                    response.addVulnerablePackage(currVulDep);
                }
            }
        }

        logger.info(String.format("Finally, found %d vulnerable dependencies", response.getVulnerableDependencies().size()));
        return ResponseEntity.ok().body(response);
    }

    /**
     * Takes a string representation of package.json file and converts it to a list of {@link Dependency}.
     * @param packageJsonString String representation of package.json file
     * @return List of {@link Dependency} object, each represent dependency in package.json
     */
    private List<Dependency> parseFileContent(final String packageJsonString) {
        List<Dependency> packages = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(packageJsonString);
            JsonNode dependenciesNode = rootNode.path(PACKAGE_JSON_DEPENDENCIES_ROOT);

            Iterator<Map.Entry<String, JsonNode>> dependenciesIterator = dependenciesNode.fields();
            while (dependenciesIterator.hasNext()) {
                Map.Entry<String, JsonNode> dependencyEntry = dependenciesIterator.next();
                String packageName = dependencyEntry.getKey();
                String packageVersion = dependencyEntry.getValue().asText();

                packages.add(new Dependency(packageName, packageVersion));
            }
        } catch (Exception e) {
            logger.error("Error occurred while trying to extract dependencies from file content", e);
        }

        return packages;
    }
}
