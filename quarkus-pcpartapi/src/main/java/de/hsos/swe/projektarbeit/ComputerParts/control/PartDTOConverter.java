package de.hsos.swe.projektarbeit.ComputerParts.control;

import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CaseDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.FanDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.GpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.MotherboardDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.PsuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.RamDTO;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;

/**
 * @author Jannis Welkener
 */
public class PartDTOConverter {

    public static Case dtoConverter(CaseDTO part) {
        Case pcCase = new Case(part.partName, part.manufacturerName);
        pcCase.internalFanAmount = part.internalFanAmount;
        pcCase.frameColor = part.frameColor;
        pcCase.sizeFactor = part.sizeFactor;
        pcCase.towerType = part.towerType;
        pcCase.hasRGB = part.hasRGB;
        pcCase.hasSeeThroughSidepanel = part.hasSeeThroughSidepanel;

        return pcCase;
    }

    public static CPU dtoConverter(CpuDTO part) {
        CPU cpu = new CPU(part.partName, part.manufacturerName);
        cpu.cpuCores = part.cpuCores;
        cpu.cpuThreads = part.cpuThreads;
        cpu.maxTurboFrequencyInGHz = part.maxTurboFrequencyInGHz;
        cpu.defaultFrequencyInGHz = part.defaultFrequencyInGHz;
        cpu.TDPinWattage = part.TDPinWattage;
        cpu.chipGeneration = part.chipGeneration;
        cpu.supportedRAMType = part.supportedRAMType;
        cpu.socket = part.socket;

        return cpu;
    }

    public static Fan dtoConverter(FanDTO part) {
        Fan fan = new Fan(part.partName, part.manufacturerName);
        fan.supportedSockets = part.supportedSockets;
        fan.fanType = part.fanType;
        fan.connections = part.connections;
        fan.fanSpeedInUmin = part.fanSpeedInUmin;
        fan.volumeIndB = part.volumeIndB;
        fan.maxCoolingInWattage = part.maxCoolingInWattage;

        return fan;
    }

    public static GPU dtoConverter(GpuDTO part) {
        GPU gpu = new GPU(part.partName, part.manufacturerName);
        gpu.amountCUDACores = part.amountCUDACores;
        gpu.chipsetManufacturer = part.chipsetManufacturer;
        gpu.clockspeedInMHz = part.clockspeedInMHz;
        gpu.gpuInterface = part.gpuInterface;
        gpu.gpuRAMType = part.gpuRAMType;
        gpu.gpuRAMinGB = part.gpuRAMinGB;
        gpu.wattage = part.wattage;

        return gpu;
    }

    public static Motherboard dtoConverter(MotherboardDTO part) {
        Motherboard mb = new Motherboard(part.partName, part.manufacturerName);
        mb.chipGeneration = part.chipGeneration;
        mb.cpuManufacturer = part.cpuManufacturer;
        mb.cpuSocket = part.cpuSocket;
        mb.hasIntegratedWLAN = part.hasIntegratedWLAN;
        mb.sizeFactor = part.sizeFactor;
        mb.storageInGB = part.storageInGB;
        mb.supportedRAMType = part.supportedRAMType;

        return mb;
    }

    public static PSU dtoConverter(PsuDTO part) {
        PSU psu = new PSU(part.partName, part.manufacturerName);
        psu.certificate = part.certificate;
        psu.connections = part.connections;
        psu.efficiencyInPercent = part.efficiencyInPercent;
        psu.isModular = part.isModular;
        psu.powerInWattage = part.powerInWattage;
        psu.sizeFactor = part.sizeFactor;

        return psu;
    }

    public static RAM dtoConverter(RamDTO part) {
        RAM ram = new RAM(part.partName, part.manufacturerName);
        ram.amountOfSticks = part.amountOfSticks;
        ram.ramSpeedInMHz = part.ramSpeedInMHz;
        ram.ramType = part.ramType;
        ram.sizePerStickInGB = part.sizePerStickInGB;
        ram.totalSizeInGB = part.totalSizeInGB;
        ram.hasRGB = part.hasRGB;

        return ram;
    }
}
