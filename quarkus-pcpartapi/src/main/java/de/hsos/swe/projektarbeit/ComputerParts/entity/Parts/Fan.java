package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class Fan extends PcPart {

    public Fan() {
    }

    public Fan(String partName_, String manufacturerName_) {
        super(PartType.FAN, partName_, manufacturerName_);
    }

    public String supportedSockets;
    public String fanType;
    public String connections;
    public int fanSpeedInUmin;
    public double volumeIndB;
    public int maxCoolingInWattage;
}
