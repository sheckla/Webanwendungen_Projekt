package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new Motherboard to the database")
public class MotherboardDTO extends PcPartDTO{

    @Schema(description = "The size of the motherboard", required = true, example = "ATX")
    @NotBlank
    public String sizeFactor;

    @Schema(description = "The manufacturer of the supported CPU (Intel or AMD)", required = true, example = "Intel")
    @NotBlank
    public String cpuManufacturer;

    @Schema(description = "The socket-type of the motherboard", required = true, example = "LGA 1200")
    @NotBlank
    public String cpuSocket;

    @Schema(description = "The type of RAM supported by the motherboard", required = true, example = "DDR4")
    @NotBlank
    public String supportedRAMType;

    @Schema(description = "The generation of the CPU", required = true, example = "10th Gen")
    @NotBlank
    public String chipGeneration;

    @Schema(description = "The internal storage-size of the motherboard in GB (e.g. 128 GB)", required = true, example = "128")
    @NotNull
    @Positive
    public int storageInGB;

    @Schema(description = "Defines whether the motherboard has a integrated WLAN module", required = true, example = "true")
    @NotBlank
    public boolean hasIntegratedWLAN;
}
