package de.hsos.swe.projektarbeit.Users.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author Jannis Welkener
 */
@Schema(description = "Schema for adding a new user")
public class UserDTO {

    @Schema(description = "Username visible by everyone (Has to be unique)", required = true, example = "uncle_bob", uniqueItems = true)
    @NotNull
    @NotBlank
    public String name;

    @Schema(description = "Your password (Keep it a secret)", required = true, example = "password123 (Not recommended)")
    @NotNull
    @NotBlank
    public String password;
}
