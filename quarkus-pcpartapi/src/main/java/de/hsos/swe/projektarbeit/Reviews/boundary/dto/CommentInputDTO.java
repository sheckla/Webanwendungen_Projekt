package de.hsos.swe.projektarbeit.Reviews.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
/**
 * @author Jannis Welkener
 */
@Schema(description = "Structure of a comment")
public class CommentInputDTO {

    @Schema(description = "The text of the comment", required = true, example = "This is a good build, but I would prefer a newer GPU for a simular price")
    @NotBlank(message="Must contain content")
    public String commentary;

    @Schema(description = "The personal rating of the pc-configuration", required = true, example = "4")
    @Min(value=1)
    @Max(value=5)
    public int userRating;
}
