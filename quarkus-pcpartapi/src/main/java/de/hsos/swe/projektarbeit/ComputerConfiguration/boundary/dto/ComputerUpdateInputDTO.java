package de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import io.smallrye.common.constraint.Nullable;
import jakarta.validation.constraints.Pattern;

/**
 * @author Daniel Graf
 */
@Schema(description = "Schema for adding & changing Part-Id's to a computer configuration")
public class ComputerUpdateInputDTO {

    @Schema(description = "GPU Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String gpuId = null;

    @Schema(description = "CPU Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String cpuId = null;

    @Schema(description = "RAM Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String ramId = null;

    @Schema(description = "Power Supply Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String psuId = null;

    @Schema(description = "Graphics Card Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String caseId = null;

    @Schema(description = "Motherboard Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String motherboardId = null;

    @Schema(description = "Fan Id of which to update the computer configuration with", example = "")
    @Nullable
    @Pattern(regexp = "[0-9a-fA-F]{24}")
    public String fanId = null;

    public ComputerUpdateInputDTO() {

    }
}
