package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class GPU extends PcPart {

    public GPU() {
    }

    public GPU(String partName_, String manufacturerName_) {
        super(PartType.GPU, partName_, manufacturerName_);
    }

    public int clockspeedInMHz;
    public String gpuRAMType;
    public int gpuRAMinGB;
    public int wattage;
    public String chipsetManufacturer;
    public String gpuInterface;
    public int amountCUDACores;

}
