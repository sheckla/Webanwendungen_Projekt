package de.hsos.swe.projektarbeit.ComputerParts.control;

import java.util.List;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;

/**
 * @author Jannis Welkener
 */
public interface ComputerPartCatalog {

    PcPart getPartByID(String id);
    void deleteById(String id);

    List<? extends PcPart> getPartByType(Class<? extends PcPart> cls);

    void saveToDB(PcPart part);

    List<PcPart> getAllPartsFromDB();
}
