package com.amiel.packageshield.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VulnerableDependency extends Dependency {
    private String severity;
    private String firstPatchedVersion;

    public VulnerableDependency(final String name, final String version, final String severity, final String firstPatchedVersion) {
        super(name, version);
        this.severity = severity;
        this.firstPatchedVersion = firstPatchedVersion;
    }
}
