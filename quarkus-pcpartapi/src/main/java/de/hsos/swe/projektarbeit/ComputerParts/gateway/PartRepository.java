package de.hsos.swe.projektarbeit.ComputerParts.gateway;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartImageService;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Jannis Welkener
 */
@ApplicationScoped
public class PartRepository implements ComputerPartCatalog, ComputerPartImageService {
    private static final Logger log = Logger.getLogger(PartRepository.class);

    public PartRepository() {
    }

    @Override
    public void saveToDB(PcPart part) {
        part.persist();
    }

    @Override
    public ArrayList<? extends PcPart> getPartByType(Class<? extends PcPart> cls) {
        ArrayList<PcPart> typedParts = new ArrayList<>();

        for (PcPart part : this.getAllPartsFromDB()) {
            if (part.getClass() == cls) {
                typedParts.add(part);
            }
        }
        return typedParts;
    }

    @Override
    public PcPart getPartByID(String id_) {
        for (PcPart part : this.getAllPartsFromDB()) {
            if (part.id.toString().equals(id_)) {
                return part;
            }
        }
        return null;
    }

    @Override
    public ArrayList<PcPart> getAllPartsFromDB() {
        ArrayList<PcPart> allParts = new ArrayList<>();
        allParts.addAll(GPU.listAll());
        allParts.addAll(CPU.listAll());
        allParts.addAll(RAM.listAll());
        allParts.addAll(Motherboard.listAll());
        allParts.addAll(PSU.listAll());
        allParts.addAll(Case.listAll());
        allParts.addAll(Fan.listAll());
        return allParts;
    }

    @Override
    public BufferedImage getImage(String partID) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.getPartByID(partID).getPartImage());
        BufferedImage bi = ImageIO.read(bais);
        return bi;
    }

    @PostConstruct
    public void init() {
        // Fill MONGO_DB if empty
        if (getAllPartsFromDB().size() <= 0) {

            GPU gpu = new GPU("GeForce RTX™ 3070 Ti GamingPro", "Palit");
            gpu.amountCUDACores = 6144;
            gpu.chipsetManufacturer = "NVidia";
            gpu.clockspeedInMHz = 1770;
            gpu.gpuInterface = "PCI-E 4.0";
            gpu.gpuRAMType = "GDDR6X";
            gpu.gpuRAMinGB = 8;
            gpu.wattage = 290;
            gpu.persist();

            CPU cpu = new CPU("Intel Core i9-10900KF", "Intel");
            cpu.cpuCores = 10;
            cpu.cpuThreads = 20;
            cpu.maxTurboFrequencyInGHz = 4.8;
            cpu.defaultFrequencyInGHz = 3.7;
            cpu.TDPinWattage = 125;
            cpu.chipGeneration = 10;
            cpu.supportedRAMType = "DDR4";
            cpu.socket = "LGA 1200";
            cpu.persist();

            RAM ram = new RAM("Ballistix RGB", "Crucial");
            ram.amountOfSticks = 2;
            ram.ramSpeedInMHz = 3200;
            ram.ramType = "DDR4";
            ram.sizePerStickInGB = 8;
            ram.totalSizeInGB = 16;
            ram.hasRGB = true;
            ram.persist();

            Case pcCase = new Case("Pure Base 500DX", "Be Quiet");
            pcCase.internalFanAmount = 3;
            pcCase.frameColor = "Black";
            pcCase.sizeFactor = "ATX";
            pcCase.towerType = "Midi Tower";
            pcCase.hasRGB = true;
            pcCase.hasSeeThroughSidepanel = true;

            pcCase.persist();

            PSU psu = new PSU("System Power 10", "be Quiet!");
            psu.certificate = "80+ Bronze";
            psu.connections = "1x ATX 20/24pol, 1x ATX12V 4+4pol, 2x PCI Express 6+2pol, 5x SATA, 1x IDE";
            psu.efficiencyInPercent = 85;
            psu.isModular = false;
            psu.powerInWattage = 450;
            psu.sizeFactor = "ATX 2.52";
            psu.persist();

            Fan fan = new Fan("Dark Rock 4 Pro", "be Quiet!");
            fan.supportedSockets = "1150, 1151, 1155, 1156, 1200, 1366, 1700, 2011, 2011-3, 2066, AM2, AM2+, AM3, AM3+, AM4, AM5, FM1, FM2, FM2+";
            fan.fanType = "Tower Cooler";
            fan.connections = "4-pin PWM";
            fan.fanSpeedInUmin = 1500;
            fan.volumeIndB = 24.3;
            fan.maxCoolingInWattage = 125;
            fan.persist();

            Motherboard motherboard = new Motherboard("TUF GAMING Z490-PLUS (WI-FI)", "ASUS");
            motherboard.chipGeneration = "Intel Z490";
            motherboard.cpuManufacturer = "Intel";
            motherboard.hasIntegratedWLAN = true;
            motherboard.sizeFactor = "ATX";
            motherboard.storageInGB = 128;
            motherboard.supportedRAMType = "DDR4";
            motherboard.cpuSocket = "LGA 1200";
            motherboard.persist();
        }
    }

    @Override
    public String setImage(String partID, InputStream stream) {
        PcPart part = this.getPartByID(partID);

        if (stream == null) {

            part.setPartImage(null);
            part.update();

            return "Image removed from " + part.getPartName();
        }

        try {
            byte[] bytes = IOUtils.toByteArray(stream);

            if (isImageFile(bytes)) {

                part.setPartImage(bytes);
                part.update();

                return "Image set successfully for " + part.getPartName();
            } else {
                return "Invalid Filetype! Make sure to only use correct image files";
            }

        } catch (IOException e) {
            return "An Error has occurred!";
        }
    }

    private static boolean isImageFile(byte[] fileToBeChecked) {

        if (fileToBeChecked.length < 8) {
            return false;
        }

        // Hackerman.
        byte[] magicNumPNG = { (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, 0x0D, (byte) 0x0A, (byte) 0x1A,
                (byte) 0x0A };
        byte[] magicNumJPG = { -1, -40, -1, -32, 0, 16, 74, 70 };

        byte[] inputMagicNumber = new byte[8];
        for (int i = 0; i < 8; i++) {
            inputMagicNumber[i] = fileToBeChecked[i];
        }
        return Arrays.equals(magicNumPNG, inputMagicNumber) || Arrays.equals(magicNumJPG, inputMagicNumber);
    }

    @Override
    public void deleteById(String id) {
        this.getPartByID(id).delete();
    }

}
