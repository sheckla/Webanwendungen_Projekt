package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class RAM extends PcPart {

    public RAM() {
    }

    public RAM(String partName_, String manufacturerName_) {
        super(PartType.RAM, partName_, manufacturerName_);
    }

    public int amountOfSticks;
    public int sizePerStickInGB;
    public String ramType;
    public int totalSizeInGB;
    public int ramSpeedInMHz;
    public boolean hasRGB;
}
