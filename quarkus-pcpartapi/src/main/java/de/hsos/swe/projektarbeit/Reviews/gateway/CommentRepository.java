package de.hsos.swe.projektarbeit.Reviews.gateway;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.gateway.ComputerRepository;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;
import de.hsos.swe.projektarbeit.ComputerParts.gateway.PartRepository;
import de.hsos.swe.projektarbeit.Reviews.control.ComputerCommentsService;
import de.hsos.swe.projektarbeit.Reviews.control.PartCommentsService;
import de.hsos.swe.projektarbeit.Reviews.control.acl.ComputerLookupAdapter;
import de.hsos.swe.projektarbeit.Reviews.control.acl.ComputerLookupService;
import de.hsos.swe.projektarbeit.Reviews.control.acl.PartLookupService;
import de.hsos.swe.projektarbeit.Reviews.entity.Comment;
import de.hsos.swe.projektarbeit.Reviews.gateway.dto.CommentOutputDTO;
import de.hsos.swe.projektarbeit.shared.ObjectIdConverter;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.UnavailableException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *         - Adapter pattern for Computer & part lookup
 */
@RequestScoped
public class CommentRepository implements PartCommentsService, ComputerCommentsService {
    private static final Logger log = Logger.getLogger(CommentRepository.class);

    // Adapter to retrieve pc status from different domain
    @Inject
    ComputerLookupService computerLookupService;

    @Inject
    PartLookupService partLookupService;

    @Inject
    PartRepository partRepository;

    /**
     * @author Jannis Welkener
     * @author Daniel Graf, added:
     *         - pc validation
     */
    public void publicComputerComment(String pc_id, String commentary, String userName, int rating)
            throws NotFoundException {

        // Validation & exceptions
        this.computerLookupService.checkIfComputerExists(pc_id);
        this.computerLookupService.checkIfComputerIsPublic(pc_id);

        Comment comment = new Comment();
        comment.parentId = pc_id;
        comment.commentary = commentary;
        comment.userName = userName;
        comment.userRating = rating;
        comment.date = LocalDateTime.now();
        log.info("New comment added to computer");
        comment.persist();
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - added Adapter
     */
    @Override
    public List<CommentOutputDTO> getComputerComments(String pcID) {
        // Validation & exceptions
        this.computerLookupService.checkIfComputerExists(pcID);
        this.computerLookupService.checkIfComputerIsPublic(pcID);

        ArrayList<CommentOutputDTO> list = new ArrayList<>();

        Comment.list("parentId", pcID).forEach(comment -> {
            CommentOutputDTO dto = new CommentOutputDTO();

            dto.commentary = ((Comment) comment).commentary;
            dto.id = ((Comment) comment).id.toString();
            dto.parentId = ((Comment) comment).parentId;
            dto.userName = ((Comment) comment).userName;
            dto.userRating = ((Comment) comment).userRating;
            dto.date = ((Comment) comment).date;

            list.add(dto);
        });

        return list;
    }

    /**
     * @author Daniel Graf
     */
    @Override
    public void deleteComputerComment(String pcId, String commentId) {
        // Validation & exceptions
        this.computerLookupService.checkIfComputerExists(pcId);

        Comment comment = Comment.findById(new ObjectId(commentId));

        if (comment == null) {
            String err = "Comment can't be found";
            log.info(err);
            throw new NotFoundException(err);
        }

        if (comment.parentId == null || comment.parentId.isEmpty()) {
            String err = "Comment has no related computer configuration";
            throw new NotFoundException(err);
        }

        if (comment.parentId != null && !comment.parentId.toString().equals(pcId)) {
            String err = "Comment not related to computer configuration";
            throw new NotFoundException(err);
        }

        comment.delete();
        log.info("Comment has been removed");
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - Part Adapter
     */
    @Override
    public List<CommentOutputDTO> getPartComments(String partId) throws NotFoundException {
        PcPart part;
        try {
            part = this.partLookupService.lookupPcPart(partId);
        } catch (NotFoundException e) {
            throw e;
        }

        // log.info("Gettings part comments for: " + part.getType() + " " + part.getPartName());

        ArrayList<CommentOutputDTO> list = new ArrayList<CommentOutputDTO>();
        Comment.list("parentId", partId).forEach(comment -> {
            CommentOutputDTO dto = new CommentOutputDTO();

            dto.commentary = ((Comment) comment).commentary;
            dto.id = ((Comment) comment).id.toString();
            dto.parentId = ((Comment) comment).parentId;
            dto.userName = ((Comment) comment).userName;
            dto.userRating = ((Comment) comment).userRating;
            dto.date = ((Comment) comment).date;

            list.add(dto);
        });
        // log.info("Part: " + part.getPartName() + " has " + list.size() + " comments");

        return list;
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - Part Adapter
     */
    @Override
    public CommentOutputDTO publishPartComment(String partId, String commentary, String username, int rating)
            throws NotFoundException {
        try {
            PcPart part = this.partLookupService.lookupPcPart(partId);
        } catch (NotFoundException e) {
            throw e;
        }

        Comment comment = new Comment();
        comment.parentId = partId;
        comment.commentary = commentary;
        comment.userName = username;
        comment.userRating = rating;
        comment.date = LocalDateTime.now();
        log.info("New comment added to part");
        comment.persist();
        CommentOutputDTO commentOutputDTO = new CommentOutputDTO();
        commentOutputDTO.commentary = ((Comment) comment).commentary;
        commentOutputDTO.id = ((Comment) comment).id.toString();
        commentOutputDTO.parentId = ((Comment) comment).parentId;
        commentOutputDTO.userName = ((Comment) comment).userName;
        commentOutputDTO.userRating = ((Comment) comment).userRating;
        commentOutputDTO.date = ((Comment) comment).date;
        return commentOutputDTO;
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - Error handling
     *         - Part adapter
     */
    @Override
    public void deletePartComment(String partId, String commentId)
            throws NotFoundException, ConstraintViolationException {
        try {
            PcPart part = this.partLookupService.lookupPcPart(partId);
        } catch (NotFoundException e) {
            throw e;
        }

        Comment comment = Comment.findById(ObjectIdConverter.fromString(commentId));

        if (comment == null) {
            String err = "Comment can't be found";
            log.info(err);
            throw new NotFoundException(err);
        }

        if (comment.parentId == null || comment.parentId.isEmpty()) {
            String err = "Comment has no related computer part";
            throw new NotFoundException(err);
        }

        if (!comment.parentId.toString().equals(partId)) {
            String err = "Comment not related to part specified";
            throw new NotFoundException(err);
        }

        comment.delete();
        log.info("Comment has been removed");
    }

}
