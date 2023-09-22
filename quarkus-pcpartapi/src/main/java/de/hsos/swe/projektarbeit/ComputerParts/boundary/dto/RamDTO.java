package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new RAM-module to the database")
public class RamDTO extends PcPartDTO{

    @Schema(description = "The amount of RAM-sticks of this module (e.g. 2-4)", required = true, example = "2")
    @NotNull
    @Min(value = 1) @Max(value = 8)
    public int amountOfSticks;

    @Schema(description = "The storage-capacity per stick in GB (e.g. 2^x)", required = true, example = "16")
    @NotNull
    @Min(value = 2) @Max(value = 128)
    public int sizePerStickInGB;

    @Schema(description = "The RAM-type (e.g. 'DDR3' / 'DDR4')", required = true, example = "DDR4")
    @NotNull
    @Size(min = 2, max = 6)
    public String ramType;

    @Schema(description = "The total storage-capacity in GB (amountOfSticks * sizePerStickInGB)", required = true, example = "32")
    @NotNull
    @Min(value = 2)
    public int totalSizeInGB;

    @Schema(description = "The storage-speed of the RAM-module in MHz (e.g. 3600 MHz)", required = true, example = "3600")
    @NotNull
    @Positive
    public int ramSpeedInMHz;

    @Schema(description = "Defines whether the RAM-Module has RGB-lights for asthetics", required = true, example = "true")
    @NotNull
    public boolean hasRGB;
}
