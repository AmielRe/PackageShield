package com.amiel.packageshield.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SecurityVulnerabilities {
    private Node[] nodes;

    @Getter
    @Setter
    public static class Node {
        private String severity;
        @JsonProperty("vulnerableVersionRange")
        private String vulnerableVersionRange;
        @JsonProperty("firstPatchedVersion")
        private PatchedVersion firstPatchedVersion;
    }

    @Getter
    @Setter
    public static class PatchedVersion {
        private String identifier;
    }
}
