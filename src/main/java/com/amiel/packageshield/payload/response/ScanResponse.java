package com.amiel.packageshield.payload.response;

import com.amiel.packageshield.models.VulnerableDependency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents all vulnerabilities found in one response object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScanResponse implements IResponse {

    @JsonProperty("vulnerablePackages")
    private List<VulnerableDependency> vulnerableDependencies;

    public void addVulnerablePackage(final VulnerableDependency vulnerableDependency) {
        vulnerableDependencies.add(vulnerableDependency);
    }
}
