package de.hsos.swe.projektarbeit.Reviews.control.acl;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;

public interface PartLookupService {
    public boolean checkIfPartExists(String partId);
    public PcPart lookupPcPart(String partId);
}
