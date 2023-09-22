package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import jakarta.json.bind.annotation.JsonbTransient;

/**
 * @author Jannis Welkener
 */
public abstract class PcPart extends PanacheMongoEntity {

    // All inherited Variables must be public for JSON-B to work (for some reason)
    // This is only a thing with PanacheEntity (why tho?)
    // final is also not allowed

    public PartType type;

    public String partName;

    public String manufacturerName;

    public boolean hasImage = false;

    // Hidden in JSON
    @JsonbTransient
    private byte[] partImage = null;

    public byte[] getPartImage() {
        return partImage;
    }

    public void setPartImage(byte[] bytes) {
        this.partImage = bytes;
        if (bytes == null) {
            this.hasImage = false;
        } else {
            this.hasImage = true;
        }
    }

    public PcPart(PartType type, String partName_, String manufacturerName_) {
        this.partName = partName_;
        this.manufacturerName = manufacturerName_;
        this.type = type;
    }

    public PcPart() {
        this.type = null;
    }

    public PartType getType() {
        return type;
    }

    public String getPartName() {
        return partName;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

}
