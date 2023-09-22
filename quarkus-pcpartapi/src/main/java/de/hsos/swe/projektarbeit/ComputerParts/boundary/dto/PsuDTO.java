package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new Power Supply Unit (PSU) to the database")
public class PsuDTO extends PcPartDTO{

    @Schema(description = "The maximum power-output of the PSU (e.g. 800 wattage)", required = true, example = "800")
    @NotNull
    @Positive
    public int powerInWattage;

    @Schema(description = "The output-efficiency in percent (e.g. 85%)", required = true, example = "85")
    @NotNull
    @Min(value = 1) @Max(value = 100)
    public int efficiencyInPercent;

    @Schema(description = "Certificate for efficient energy usage (e.g. 80 Plus Bronze, 80 Plus Silver, 80 Plus Gold)", required = true, example = "80 Plus Gold")
    @NotNull
    public String certificate;

    @Schema(description = "The types of connection cables supported by the PSU", required = true, example = "1x ATX 20/24pol, 1x ATX12V 4+4pol, 2x PCI Express 6+2pol, 5x SATA, 1x IDE")
    @NotNull
    public String connections;

    @Schema(description = "The size-factor of the module (e.g. ATX)", required = true, example = "ATX")
    @NotNull
    public String sizeFactor;

    @Schema(description = "Defines whether the PSU has detachable cables or not", required = true, example = "false")
    @NotNull
    public boolean isModular;

}
