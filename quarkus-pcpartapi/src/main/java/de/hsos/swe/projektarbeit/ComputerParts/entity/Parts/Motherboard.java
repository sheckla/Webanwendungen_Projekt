package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class Motherboard extends PcPart {

    public Motherboard() {
    }

    public Motherboard(String partName_, String manufacturerName_) {
        super(PartType.MOTHERBOARD, partName_, manufacturerName_);
    }

    public String sizeFactor;
    public String cpuManufacturer;
    public String cpuSocket;
    public String supportedRAMType;
    public String chipGeneration;
    public int storageInGB;
    public boolean hasIntegratedWLAN;

}
