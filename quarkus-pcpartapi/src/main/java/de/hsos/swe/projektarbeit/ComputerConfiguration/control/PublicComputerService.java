package de.hsos.swe.projektarbeit.ComputerConfiguration.control;

import java.util.List;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerDTOReduced;

/**
 * @author Jannis Welkener
 * @author Daniel Graf, additionally:
 *         - interface seggregation pattern
 */
public interface PublicComputerService {

    List<ComputerDTOReduced> getAllPublicComputersAsDTO();

    ComputerDTO getPublicPcByID(String id);

}
