package de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.entity.Computer;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;
import de.hsos.swe.projektarbeit.Reviews.entity.Comment;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Jannis Welkener
 */
@ApplicationScoped
public class ComputerConverter {

    // @Inject TODO
    ComputerPartCatalog repository;

    public ComputerConverter(ComputerPartCatalog repository_) {
        this.repository = repository_;
    }

    public ComputerDTO pcIDsToObjects(Computer pc) {

        ComputerDTO dto = new ComputerDTO();

        dto.pcOwner = pc.pcOwner;
        dto.pcName = pc.pcName;

        dto.pc_case = (Case) repository.getPartByID(pc.caseID);
        dto.cpu = (CPU) repository.getPartByID(pc.cpuID);
        dto.gpu = (GPU) repository.getPartByID(pc.gpuID);
        dto.fan = (Fan) repository.getPartByID(pc.fanID);
        dto.ram = (RAM) repository.getPartByID(pc.ramID);
        dto.motherboard = (Motherboard) repository.getPartByID(pc.motherboardID);
        dto.psu = (PSU) repository.getPartByID(pc.psuID);

        dto.id = pc.id.toString();

        dto.isPublic = pc.isPublic;

        // Comments
        dto.commentAmount = Comment.list("parentId", dto.id).size();
        dto.userRating = 0;
        if (dto.commentAmount > 0) {
            Comment.list("parentId", dto.id).forEach(r -> {
                dto.userRating += ((Comment) r).userRating;
            });
            dto.userRating /= dto.commentAmount;
        }
        return dto;
    }

    public ComputerDTOReduced pcIDsToNames(Computer pc) {

        ComputerDTOReduced dto = new ComputerDTOReduced();

        dto.pcOwner = pc.pcOwner;
        dto.pcName = pc.pcName;

        // TODO performance optimization
        if (repository.getPartByID(pc.caseID) != null) {
            dto.pc_case = repository.getPartByID(pc.caseID).partName;
        }
        if (repository.getPartByID(pc.cpuID) != null) {
            dto.cpu = repository.getPartByID(pc.cpuID).partName;
        }
        if (repository.getPartByID(pc.gpuID) != null) {
            dto.gpu = repository.getPartByID(pc.gpuID).partName;
        }
        if (repository.getPartByID(pc.fanID) != null) {
            dto.fan = repository.getPartByID(pc.fanID).partName;
        }
        if (repository.getPartByID(pc.ramID) != null) {
            dto.ram = repository.getPartByID(pc.ramID).partName;
        }
        if (repository.getPartByID(pc.motherboardID) != null) {
            dto.motherboard = repository.getPartByID(pc.motherboardID).partName;
        }
        if (repository.getPartByID(pc.psuID) != null) {
            dto.psu = repository.getPartByID(pc.psuID).partName;
        }

        dto.id = pc.id.toString();

        dto.isPublic = pc.isPublic;

        // Comments
        dto.commentAmount = Comment.list("parentId", dto.id).size();
        dto.userRating = 0;
        if (dto.commentAmount > 0) {
            Comment.list("parentId", dto.id).forEach(r -> {
                dto.userRating += ((Comment) r).userRating;
            });
            dto.userRating /= dto.commentAmount;
        }

        return dto;
    }
}
