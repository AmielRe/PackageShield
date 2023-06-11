package com.amiel.packageshield.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanRequest {
    @NotBlank
    private String ecosystem;

    @NotBlank
    private String fileContent;
}
