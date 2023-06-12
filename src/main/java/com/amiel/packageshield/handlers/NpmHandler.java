package com.amiel.packageshield.handlers;

import com.amiel.packageshield.models.Dependency;
import com.amiel.packageshield.payload.response.IResponse;
import com.amiel.packageshield.payload.response.ScanResponse;
import com.amiel.packageshield.models.VulnerableDependency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;

@Component("npm")
public class NpmHandler extends BaseHandler {

    @Override
    protected ResponseEntity<IResponse> customHandleLogic(final String decodedFileContent, final String ecoSystem) {
        ScanResponse response = new ScanResponse(new ArrayList<>());
        List<Dependency> dependenciesToScan = parseFileContent(decodedFileContent);
        for (Dependency dep : dependenciesToScan) {
            List<VulnerableDependency> vulnerableDependencies = super.scanWithAllScanners(dep, ecoSystem);

            for (VulnerableDependency currVulDep : vulnerableDependencies) {
                if(response.getVulnerableDependencies().stream().noneMatch(vulnerableDependency -> vulnerableDependency.getName().equalsIgnoreCase(currVulDep.getName()))){
                    response.addVulnerablePackage(currVulDep);
                }
            }
        }

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
            JsonNode dependenciesNode = rootNode.path("dependencies");

            Iterator<Map.Entry<String, JsonNode>> dependenciesIterator = dependenciesNode.fields();
            while (dependenciesIterator.hasNext()) {
                Map.Entry<String, JsonNode> dependencyEntry = dependenciesIterator.next();
                String packageName = dependencyEntry.getKey();
                String packageVersion = dependencyEntry.getValue().asText();

                packages.add(new Dependency(packageName, packageVersion));
            }
        } catch (IOException e) {
            // Todo: Log later
        }

        return packages;
    }
}
