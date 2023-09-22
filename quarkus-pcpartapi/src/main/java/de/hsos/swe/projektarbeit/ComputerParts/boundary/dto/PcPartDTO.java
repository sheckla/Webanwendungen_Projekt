package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
/**
 * @author Jannis Welkener
 */
public class PcPartDTO {

    @Schema(description = "The name of the part", required = true, example = "Geforce RTX 3070 Ti")
    @NotBlank
    public String partName;

    @Schema(description = "The manufacturer of the part", required = true, example = "Nvidia")
    @NotBlank
    public String manufacturerName;
}
