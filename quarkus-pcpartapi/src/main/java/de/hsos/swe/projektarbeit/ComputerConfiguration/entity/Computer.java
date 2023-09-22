package de.hsos.swe.projektarbeit.ComputerConfiguration.entity;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

/**
 * @author Jannis Welkener
 */
public class Computer extends PanacheMongoEntity {
    public String pcName = "Awesome PC";
    public String pcOwner = "FritzDerWitz";

    public Computer() {
    }

    public String gpuID;
    public String cpuID;
    public String ramID;
    public String psuID;
    public String caseID;
    public String motherboardID;
    public String fanID;

    public boolean isPublic = false;

    public Computer(String g, String c, String r, String p, String c_, String m, String f) {
        gpuID = g;
        cpuID = c;
        ramID = r;
        psuID = p;
        caseID = c_;
        motherboardID = m;
        fanID = f;
    }

    public void setIdByPartType(PartType type, String partId) {
        switch (type) {
            case CPU:
                cpuID = partId;
                break;
            case GPU:
                gpuID = partId;
                break;
            case MOTHERBOARD:
                motherboardID = partId;
                break;
            case CASE:
                caseID = partId;
                break;
            case FAN:
                fanID = partId;
                break;
            case PSU:
                psuID = partId;
                break;
            case RAM:
                ramID = partId;
                break;
        }
    }

    public String getIdByPartType(PartType type) {
        switch (type) {
            case CPU:
                return cpuID;
            case GPU:
                return gpuID;
            case MOTHERBOARD:
                return motherboardID;
            case CASE:
                return caseID;
            case FAN:
                return fanID;
            case PSU:
                return psuID;
            case RAM:
                return ramID;
        }
        return null;
    }

    // ?? Here or in own folder
    // public int commentAmount = 1;
    // public int userRating = 5;
    // userRating/5 (General information)
    // public static final int maxRating = 5;

}
