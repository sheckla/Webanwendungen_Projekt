package de.hsos.swe.projektarbeit.Reviews.control;

import java.util.List;

import de.hsos.swe.projektarbeit.Reviews.gateway.dto.CommentOutputDTO;
import jakarta.resource.spi.UnavailableException;

/**
 * @author Daniel Graf
 */
public interface ComputerCommentsService {
    void publicComputerComment(String pcID, String commentary, String userName, int rating) throws UnavailableException;

    List<CommentOutputDTO> getComputerComments(String pcID);

    public void deleteComputerComment(String pcId, String commentId);
}
