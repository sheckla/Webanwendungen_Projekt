package de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.DefaultValue;

/**
 * @author Daniel Graf
 */
@Schema(description = "Schema for setting the visibility of a computer configuration")
public class VisibilityInputDTO {
    @NotNull
    @Schema(description = "Sets if the computer configuration can only be seen by your user account or by all other users", required = true, example = "true", defaultValue = "false")
    public Boolean isPublic;


    public VisibilityInputDTO() {

    }
}
