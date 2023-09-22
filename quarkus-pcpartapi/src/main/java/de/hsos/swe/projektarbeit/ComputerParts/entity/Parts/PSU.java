package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class PSU extends PcPart {

    public PSU() {
    }

    public PSU(String partName_, String manufacturerName_) {
        super(PartType.PSU, partName_, manufacturerName_);
    }

    public int powerInWattage;

    public int efficiencyInPercent;

    public String certificate;

    public String connections;

    public String sizeFactor;

    public boolean isModular;

}
