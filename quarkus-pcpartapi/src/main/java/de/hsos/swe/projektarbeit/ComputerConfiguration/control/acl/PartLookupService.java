package de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;

/**
 * @author Daniel Graf
 */
public interface PartLookupService {
    public boolean checkIfPartExists(String partId);
    public PcPart lookupPcPart(String partId);
}
