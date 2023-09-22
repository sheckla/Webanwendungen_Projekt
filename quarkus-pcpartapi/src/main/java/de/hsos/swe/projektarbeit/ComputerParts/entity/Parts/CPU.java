package de.hsos.swe.projektarbeit.ComputerParts.entity.Parts;

/**
 * @author Jannis Welkener
 */
public class CPU extends PcPart{

    public CPU(){}

    public CPU(String partName_,String manufacturerName_) {
        super(PartType.CPU,partName_,manufacturerName_);
    }

    public int cpuCores;
    public int cpuThreads;
    public double maxTurboFrequencyInGHz;
    public double defaultFrequencyInGHz;
    public int TDPinWattage;
    public int chipGeneration;
    public String supportedRAMType;
    public String socket;

}
