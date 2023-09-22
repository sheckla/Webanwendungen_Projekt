package de.hsos.swe.projektarbeit.Reviews.gateway.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Jannis Welkener
 */
public class CommentOutputDTO {

    public String id;
    public String parentId;
    public static final int maxRating = 5;
    public int userRating;
    public String userName;
    public String commentary;
    public LocalDateTime date;

    public CommentOutputDTO() {
    }



}
