package de.hsos.swe.projektarbeit.Reviews.control;

import java.util.List;

import de.hsos.swe.projektarbeit.Reviews.gateway.dto.CommentOutputDTO;

/**
 * @author Daniel Graf
 */
public interface PartCommentsService {
    public CommentOutputDTO publishPartComment(String partId, String commentary, String username, int rating);

    List<CommentOutputDTO> getPartComments(String partId);

    public void deletePartComment(String partId, String commentId);

}
