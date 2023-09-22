package de.hsos.swe.projektarbeit.Reviews.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


import io.quarkus.mongodb.panache.PanacheMongoEntity;
/**
 * @author Jannis Welkener
 */
public class Comment extends PanacheMongoEntity{
    public String parentId;
    public static final int maxRating = 5;
    public int userRating;
    public String userName;
    public String commentary;
    public LocalDateTime date;


    public Comment(){}

    @Override
    public String toString() {
        return "Comment [parentId=" + parentId + ", userRating=" + userRating + ", userName=" + userName
                + ", commentary=" + commentary + ", date=" + date + "]";
    }
}
