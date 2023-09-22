package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class Case extends PcPart {

    public Case() {
    }

    public Case(String partName_, String manufacturerName_) {
        super(PartType.CASE, partName_, manufacturerName_);
    }

    public int internalFanAmount;
    public String frameColor;
    public String sizeFactor;
    public String towerType;
    public boolean hasRGB;
    public boolean hasSeeThroughSidepanel;
}
