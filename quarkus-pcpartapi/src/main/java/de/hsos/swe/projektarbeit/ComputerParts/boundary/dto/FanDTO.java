package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new CPU-Fan to the database")
public class FanDTO extends PcPartDTO{

    @Schema(description = "The supported CPU-socket (e.g. LGA 1200)", required = true, example = "1150, 1151, 1155, 1200")
    @NotBlank
    public String supportedSockets;

    @Schema(description = "The type of fan (e.g. Tower Cooler / AIO)", required = true, example = "Tower Cooler")
    @NotBlank
    public String fanType;

    @Schema(description = "The connection cables for power", required = true, example = "4-pin PWM")
    @NotBlank
    public String connections;

    @Schema(description = "The rotation speed of the fan in Umin", required = true, example = "1500")
    @NotNull
    @Positive
    public int fanSpeedInUmin;

    @Schema(description = "The sound volume in dB", required = true, example = "24.3")
    @NotNull
    @Positive
    public double volumeIndB;

    @Schema(description = "The maximal cooling capacity in wattage", required = true, example = "24.3")
    @NotNull
    @Positive
    public int maxCoolingInWattage;
}
