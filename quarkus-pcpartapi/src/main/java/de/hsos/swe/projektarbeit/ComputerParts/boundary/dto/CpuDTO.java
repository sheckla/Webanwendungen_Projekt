package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new CPU to the database")
public class CpuDTO extends PcPartDTO{

    @Schema(description = "The amount of physical cores", required = true, example = "10")
    @NotNull
    @Positive
    public int cpuCores;

    @Schema(description = "The amount of logical cores (Typically 2 * cpuCores)", required = true, example = "20")
    @NotNull
    @Positive
    public int cpuThreads;

    @Schema(description = "The maximal turbo frequency before thermal throtteling in GHz", required = true, example = "4.8")
    @NotNull
    @Positive
    public double maxTurboFrequencyInGHz;

    @Schema(description = "The default frequency in GHz", required = true, example = "3.7")
    @NotNull
    @Positive
    public double defaultFrequencyInGHz;

    @Schema(description = "The heat-output in wattage", required = true, example = "125")
    @NotNull
    @Positive
    public int TDPinWattage;

     @Schema(description = "The generation of the chipset", required = true, example = "10")
    @NotNull
    @Positive
    public int chipGeneration;

     @Schema(description = "The RAM-Type supported by the chipset", required = true, example = "DDR4")
    @NotBlank
    public String supportedRAMType;

    @Schema(description = "The type of socket used for this chip", required = true, example = "LGA 1200")
    @NotBlank
    public String socket;
}
