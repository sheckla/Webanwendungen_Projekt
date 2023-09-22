package de.hsos.swe.projektarbeit.ComputerParts.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new PC-Case to the database")
public class CaseDTO extends PcPartDTO{

    @Schema(description = "The amount of fans already shipped with this case", required = true, example = "3")
    @NotBlank
    @PositiveOrZero
    public int internalFanAmount;

    @Schema(description = "The color of the frame", required = true, example = "black")
    @NotBlank
    public String frameColor;

    @Schema(description = "The size-factor of the frame", required = true, example = "ATX")
    @NotBlank
    public String sizeFactor;

    @Schema(description = "The type of the frame", required = true, example = "MIDI-Tower")
    @NotBlank
    public String towerType;

    @Schema(description = "Defines whether the case has integrated RGB-lights", required = false, example = "true")
    public boolean hasRGB;

    @Schema(description = "Defines whether the case has a seethrough side-panel to view the internal components", required = false, example = "true")
    public boolean hasSeeThroughSidepanel;
}
