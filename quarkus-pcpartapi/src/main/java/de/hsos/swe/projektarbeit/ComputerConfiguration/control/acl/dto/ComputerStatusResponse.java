package de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Jannis Welkener
 */
public class ComputerStatusResponse {
    public String gpuStatus;
    public String cpuStatus;
    public String ramStatus;
    public String psuStatus;
    public String caseStatus;
    public String motherboardStatus;
    public String fanStatus;

    public String infoMessage;
    @Schema(hidden = true)
    public Status code;

    public ComputerStatusResponse() {

    }

    public void setStatusMessageByType(PartType type, String msg) {
        switch (type) {
            case CPU:
                cpuStatus = msg;
                break;
            case GPU:
                gpuStatus = msg;
                break;
            case MOTHERBOARD:
                motherboardStatus = msg;
                break;
            case CASE:
                caseStatus = msg;
                break;
            case FAN:
                fanStatus = msg;
                break;
            case PSU:
                psuStatus = msg;
                break;
            case RAM:
                ramStatus = msg;
                break;
        }
    }
}
