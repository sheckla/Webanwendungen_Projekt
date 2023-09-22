package de.hsos.swe.projektarbeit.ComputerConfiguration.control;

import java.util.List;

import org.bson.types.ObjectId;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerUpdateInputDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerDTOReduced;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerStatusResponse;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;

/**
 * @author Jannis Welkener
 * @author Daniel Graf, additionally:
 *         - interface seggregation pattern
 */
public interface PrivateComputerService {

    List<ComputerDTOReduced> getAllPrivateComputersAsDTO(String name);

    ComputerDTOReduced createNewPC(String owner, String pcName);

    ComputerDTO getPrivatePcByID(String username, String id);

    ComputerDTOReduced createNewPcRandom(String owner, String pcName);

    ComputerStatusResponse publishPC(String username, ObjectId id);

    ComputerStatusResponse hideComputer(String username, ObjectId id);

    ComputerStatusResponse setPrivateComputerComponentsById(String username, ObjectId pcId,
            ComputerUpdateInputDTO computerUpdateDTO);

    ComputerStatusResponse setPrivateComputerComponentByPartType(String username, ObjectId pcId, String partId,
            PartType type);

    ComputerStatusResponse deletePrivateComputerComponentByPartType(String username, ObjectId id, PartType type);

    ComputerStatusResponse deletePrivateComputerConfiguration(String username, ObjectId id);

}
