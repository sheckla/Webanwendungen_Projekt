package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new Graphicscard (GPU) to the database")
public class GpuDTO extends PcPartDTO{

    @Schema(description = "The maximum clockspeed of the GPU in MHz (e.g. 1770 MHz)", required = true, example = "1770")
    @NotNull
    @Positive
    public int clockspeedInMHz;

    @Schema(description = "The type of the integrated RAM", required = true, example = "GDDR6X")
    @NotBlank
    public String gpuRAMType;

    @Schema(description = "The storage capacity of the integrated RAM (e.g. 8 GB)", required = true, example = "8")
    @NotNull
    @Positive
    public int gpuRAMinGB;

    @Schema(description = "The power consumption of the GPU (e.g. 290 wattage)", required = true, example = "290")
    @NotNull
    @Positive
    public int wattage;

    @Schema(description = "The manufacturer of the type of card (Nvidia / AMD / Intel)", required = true, example = "Nvidia")
    @NotBlank
    public String chipsetManufacturer;

    @Schema(description = "The connection interface to the motherboard", required = true, example = "PCI-E 4.0")
    @NotBlank
    public String gpuInterface;

    @Schema(description = "The amount of integrated CUDA-cores (optional)", required = false, example = "6144")
    @PositiveOrZero
    public int amountCUDACores;
}
