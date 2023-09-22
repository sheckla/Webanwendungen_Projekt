package de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl;

import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;
import de.hsos.swe.projektarbeit.ComputerParts.gateway.PartRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

/**
 * @author Daniel Graf
 */
public class PartLookupAdapter implements PartLookupService{
    private static final Logger log = Logger.getLogger(PartLookupAdapter.class);
    private PcPart partLookup;

    @Inject
    PartRepository partRepository;

    @Override
    public boolean checkIfPartExists(String partId) throws NotFoundException {
        log.info("Lookup for part from reviews module");
        PcPart part = this.partRepository.getPartByID(partId);

        if (part == null) {
            throw new NotFoundException("Part lookup: not found");
        }
        this.partLookup = part;
        return true;
    }

    @Override
    public PcPart lookupPcPart(String partId) throws NotFoundException {
        if (this.partLookup != null) {
            return this.partLookup;
        }
        try {
            this.checkIfPartExists(partId);
        } catch (NotFoundException e) {
            String msg = "Part retrieval not successfull, doesn't exist";
            log.info(msg);
            throw new NotFoundException(msg, e);
        }

        return this.partLookup;
    }

}
