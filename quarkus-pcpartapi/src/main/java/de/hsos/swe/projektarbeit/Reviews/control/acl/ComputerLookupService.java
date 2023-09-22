package de.hsos.swe.projektarbeit.Reviews.control.acl;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;

/**
 * @author Daniel Graf
 */
public interface ComputerLookupService {
    public boolean checkIfComputerExists(String pcId);

    public boolean checkIfComputerIsPublic(String pcId);
}
