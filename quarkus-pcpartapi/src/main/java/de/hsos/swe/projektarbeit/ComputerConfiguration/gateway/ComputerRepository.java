package de.hsos.swe.projektarbeit.ComputerConfiguration.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerUpdateInputDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.ComputerValidationService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.PrivateComputerService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.PublicComputerService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerConverter;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerDTOReduced;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerStatusResponse;
import de.hsos.swe.projektarbeit.ComputerConfiguration.entity.Computer;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;
import de.hsos.swe.projektarbeit.ComputerParts.gateway.PartRepository;
import de.hsos.swe.projektarbeit.Reviews.gateway.CommentRepository;
import de.hsos.swe.projektarbeit.Reviews.gateway.dto.CommentOutputDTO;
import de.hsos.swe.projektarbeit.shared.ObjectIdConverter;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * =====================================================
 * | Computer Configuration Repository (MongoDB)
 * =====================================================
 *
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *         - logging
 *         - input validation
 *         - response messages
 *         - Exception handling
 */
@RequestScoped
public class ComputerRepository implements PublicComputerService, PrivateComputerService, ComputerValidationService {
    private static final Logger log = Logger.getLogger(ComputerRepository.class);

    @Inject
    ComputerPartCatalog partRepository;

    @Inject
    CommentRepository commentRepository;

    @Inject
    ComputerConverter computerConverter;

    /**
     * @author Jannis Welkener
     */
    @Override
    public List<ComputerDTOReduced> getAllPrivateComputersAsDTO(String name) {
        ArrayList<ComputerDTOReduced> dto = new ArrayList<>();

        // WHERE pcOwner == name
        Computer.find("pcOwner", name).list().forEach(pc -> {
            dto.add(this.computerConverter.pcIDsToNames((Computer) pc));
        });
        return dto;
    }

    /**
     * @author Jannis Welkener
     */
    @Override
    public List<ComputerDTOReduced> getAllPublicComputersAsDTO() {
        ArrayList<ComputerDTOReduced> dto = new ArrayList<>();

        Computer.find("isPublic", true).list().forEach(pc -> {
            dto.add(this.computerConverter.pcIDsToNames((Computer) pc));
        });
        return dto;
    }

    /**
     * @author Jannis Welkener
     */
    @Override
    public ComputerDTOReduced createNewPC(String owner, String pcName) {
        Computer pc = new Computer();
        pc.pcOwner = owner;
        pc.pcName = pcName;
        pc.persist();
        log.info("New Computer configuration (" + pcName + ") was created for " + owner);
        return this.computerConverter.pcIDsToNames(pc);
    }

    /**
     * TODO only for testing, to be removed or added correctly
     *
     * enabled by application.properties:
     * - pcpart.api.experimental.enabled
     *
     * @author Daniel Graf
     */
    @Override
    public ComputerDTOReduced createNewPcRandom(String owner, String pcName) {

        List<CPU> cpus = (List<CPU>) partRepository.getPartByType(CPU.class);
        List<GPU> gpus = (List<GPU>) partRepository.getPartByType(GPU.class);
        List<RAM> rams = (List<RAM>) partRepository.getPartByType(RAM.class);
        List<PSU> psus = (List<PSU>) partRepository.getPartByType(PSU.class);
        List<Case> cases = (List<Case>) partRepository.getPartByType(Case.class);
        List<Motherboard> motherboards = (List<Motherboard>) partRepository.getPartByType(Motherboard.class);
        List<Fan> fans = (List<Fan>) partRepository.getPartByType(Fan.class);

        CPU cpu = cpus.get(new Random().nextInt(cpus.size()));
        GPU gpu = gpus.get(new Random().nextInt(gpus.size()));
        RAM ram = rams.get(new Random().nextInt(rams.size()));
        PSU psu = psus.get(new Random().nextInt(psus.size()));
        Case pc_case = cases.get(new Random().nextInt(cases.size()));
        Motherboard mb = motherboards.get(new Random().nextInt(motherboards.size()));
        Fan fan = fans.get(new Random().nextInt(fans.size()));

        Computer pc = new Computer(gpu.id.toString(), cpu.id.toString(), ram.id.toString(), psu.id.toString(),
                pc_case.id.toString(), mb.id.toString(), fan.id.toString());
        pc.pcOwner = owner;
        pc.pcName = pcName;
        pc.persist();

        return this.computerConverter.pcIDsToNames(pc);
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse publishPC(String username, ObjectId id) {
        Computer pc = Computer.findById(id);
        ComputerStatusResponse response = new ComputerStatusResponse();

        if (!this.computerConfigurationExists(pc, response)) {
            return response;
        }

        if (!this.userOwnsComputer(pc, username)) {
            return response;
        }

        pc.isPublic = true;
        String msg = "Computer configuration has been set to public. It's now viewable by all users";
        response.infoMessage = msg;
        response.code = Status.OK;
        log.info(msg);

        pc.update();
        return response;
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     */
    @Override
    public ComputerDTO getPrivatePcByID(String username, String id) {
        ObjectId objectId = ObjectIdConverter.fromString(id);
        Computer pc = Computer.findById(objectId);

        if (!this.computerConfigurationExists(pc)) {
            return null;
        }

        if (!this.userOwnsComputer(pc, username)) {
            return null;
        }

        return this.computerConverter.pcIDsToObjects(pc);
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     */
    @Override
    public ComputerDTO getPublicPcByID(String id) {
        ObjectId objectId = ObjectIdConverter.fromString(id);
        Computer pc = Computer.findById(objectId);

        if (!this.computerConfigurationExists(pc)) {
            return null;
        }

        if (!pc.isPublic) {
            return null;
        }

        return this.computerConverter.pcIDsToObjects(pc);
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse setPrivateComputerComponentsById(String username, ObjectId pcId,
            ComputerUpdateInputDTO updatedIds) {
        ComputerStatusResponse response = new ComputerStatusResponse();
        Computer pc = Computer.findById(pcId);

        if (!this.computerConfigurationExists(pc, response)) {
            return response;
        }

        if (!this.userOwnsComputer(pc, username, response)) {
            return response;
        }

        this.setComputerComponentByPartType(pc, updatedIds.gpuId, response, PartType.GPU);
        this.setComputerComponentByPartType(pc, updatedIds.cpuId, response, PartType.CPU);
        this.setComputerComponentByPartType(pc, updatedIds.ramId, response, PartType.RAM);
        this.setComputerComponentByPartType(pc, updatedIds.psuId, response, PartType.PSU);
        this.setComputerComponentByPartType(pc, updatedIds.motherboardId, response, PartType.MOTHERBOARD);
        this.setComputerComponentByPartType(pc, updatedIds.fanId, response, PartType.FAN);

        pc.update();
        return response;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse deletePrivateComputerComponentByPartType(String username, ObjectId pcId,
            PartType type) {
        ComputerStatusResponse responseDTO = new ComputerStatusResponse();
        Computer pc = Computer.findById(pcId);

        if (!this.computerConfigurationExists(pc, responseDTO)) {
            return responseDTO;
        }

        if (!this.userOwnsComputer(pc, username, responseDTO)) {
            return responseDTO;
        }

        // check if part has been assigned
        String partId = pc.getIdByPartType(type);
        PcPart part = this.partRepository.getPartByID(partId);
        if (partId == null || part == null) {
            String infoMsg = "Part hasn't been assigned or has already been deleted.";
            responseDTO.infoMessage = infoMsg;
            responseDTO.code = Status.NOT_FOUND;
            log.info(infoMsg);
            return responseDTO;
        }

        // delete assigned part
        pc.setIdByPartType(type, null);
        String infoMsg = type + " from PC:" + pc.pcName + " has been deleted";
        responseDTO.infoMessage = infoMsg;
        responseDTO.code = Status.OK;
        log.info(infoMsg);

        pc.update();
        return responseDTO;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse deletePrivateComputerConfiguration(String username, ObjectId id) {
        ComputerStatusResponse response = new ComputerStatusResponse();
        Computer pc = Computer.findById(id);

        if (!this.computerConfigurationExists(pc, response)) {
            return response;
        }

        if (!this.userOwnsComputer(pc, username, response)) {
            return response;
        }

        String infoMsg = "PC:" + pc.pcName + " has been deleted";
        response.infoMessage = infoMsg;
        response.code = Status.OK;
        log.info(infoMsg);

        // TODO ACL
        List<CommentOutputDTO> commentsList = this.commentRepository.getComputerComments(id.toString());
        if (commentsList.size() > 0) {
            log.info("Comments found for pc, also deleting...");
            commentsList.forEach(c -> {
                log.info("deleting comment: " + c.commentary);
                this.commentRepository.deleteComputerComment(id.toString(), c.id);
            });
            log.info("All " + commentsList.size() + " comments deleted");
        } else {
            log.info("No comments found for this configuration");
        }

        pc.delete();
        return response;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse hideComputer(String username, ObjectId id) {
        ComputerStatusResponse response = new ComputerStatusResponse();
        Computer pc = Computer.findById(id);

        if (!this.computerConfigurationExists(pc, response)) {
            return response;
        }

        if (!this.userOwnsComputer(pc, username, response)) {
            return response;
        }

        String infoMsg = "PC:" + pc.pcName + " has been set to private";
        response.infoMessage = infoMsg;
        response.code = Status.OK;
        pc.isPublic = false;
        log.info(infoMsg);

        pc.update();
        return response;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public ComputerStatusResponse setPrivateComputerComponentByPartType(String username, ObjectId pcId, String partId,
            PartType type) {
        ComputerStatusResponse response = new ComputerStatusResponse();
        Computer pc = Computer.findById(pcId);

        if (!this.computerConfigurationExists(pc, response)) {
            return response;
        }

        if (!this.userOwnsComputer(pc, username, response)) {
            return response;
        }

        if (this.setComputerComponentByPartType(pc, partId, response, type)) {
            log.info("New Computer part was set for the configuration");
        }
        pc.update();
        return response;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public boolean computerConfigurationIsPublic(String pcId) throws ValidationException {
        // convert and check for valid id
        ObjectId pcObjectId = ObjectIdConverter.fromString(pcId);
        Computer pc = Computer.findById(pcObjectId);
        return pc.isPublic;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public boolean computerConfigurationExists(String pcId) throws ValidationException {
        // check for valid pcId
        ObjectId pcObjectId = ObjectIdConverter.fromString(pcId);

        Computer pc = Computer.findById(pcObjectId);
        return this.computerConfigurationExists(pc);
    }

    /*****************************
     * Private Helper Functions
     *****************************/

    /**
     * @author Daniel Graf
     */
    private boolean computerConfigurationExists(Computer pc) {
        return this.computerConfigurationExists(pc, null);
    }

    /**
     * @author Daniel Graf
     */
    private boolean computerConfigurationExists(Computer pc, ComputerStatusResponse response) {
        if (pc == null) {
            String msg = "Computer configuration could not be found";
            log.info(msg);
            if (response != null) {
                response.code = Status.NOT_FOUND;
                response.infoMessage = msg;
            }
            return false;
        }
        return true;
    }

    /**
     * @author Daniel Graf
     */
    private boolean userOwnsComputer(Computer pc, String username) {
        return this.userOwnsComputer(pc, username, null);
    }

    /**
     * @author Daniel Graf
     */
    private boolean userOwnsComputer(Computer pc, String username, ComputerStatusResponse response) {
        if (!pc.pcOwner.equals(username)) {
            String msg = "Pc exists but does not belong to user [" + pc.pcOwner + " != " + username + "]";
            log.info(msg);
            if (response != null) {
                response.infoMessage = msg;
                response.code = Status.UNAUTHORIZED;
            }
            return false;
        }
        return true;
    }

    /**
     * Set Computer part via partId
     * - also adds info to the response
     *
     * @author Daniel Graf
     */
    private boolean setComputerComponentByPartType(Computer pc, String partId, ComputerStatusResponse response,
            PartType partType) {

        // null check
        if (partId == null) {
            response.setStatusMessageByType(partType, null);
            return false;
        }

        // empty string check
        if (partId.equals("")) {
            response.setStatusMessageByType(partType, "unchanged, empty id was given");
            return false;
        }

        // same string check
        if (pc.getIdByPartType(partType) != null && pc.getIdByPartType(partType).equals(partId)) {
            response.setStatusMessageByType(partType, "unchanged, given id is already set for this part");
            return false;
        }

        // check for valid id
        ObjectId partObjectId;
        try {
            partObjectId = ObjectIdConverter.fromString(partId);
        } catch (IllegalArgumentException e) {
            log.info("New " + partType + "-ID is invalid." + e.toString());
            response.setStatusMessageByType(partType, "unchanged, id given was invalid");
            return false;
        }

        // check if id matches to the updated pc-component (GPU == GPU etc.)
        if (!this.partIdEqualsPartType(partId, partType)) {
            log.info("New " + partType + " update request doesn't match the type: " + partType);
            response.setStatusMessageByType(partType,
                    "unchanged, id given points to a different component type than what is specified");
            return false;
        }

        // update part type with new id
        String infoMsg;
        if (pc.getIdByPartType(partType) == null) {
            infoMsg = "new part was added";
        } else {
            infoMsg = "old part was successfully overwritten";
        }
        response.setStatusMessageByType(partType, infoMsg);
        pc.setIdByPartType(partType, partId);
        log.info(partType + " updated with id: " + pc.getIdByPartType(partType));
        return true;
    }

    /**
     * // lookup if given partId finds a part & if that part matches the specified
     * type
     *
     * TODO ACL because of part lookup
     *
     * @author Daniel Graf
     */
    private boolean partIdEqualsPartType(String partId, PartType type) {
        ObjectId objectId;
        try {
            objectId = ObjectIdConverter.fromString(partId);
        } catch (IllegalArgumentException e) {
            return false;
        }
        PartRepository repo = new PartRepository();
        PcPart part = repo.getPartByID(objectId.toString());
        return ((part != null) && type == part.type);
    }
}
