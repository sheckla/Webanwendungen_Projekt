package de.hsos.swe.projektarbeit.shared;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ValidationException;
/**
 * @author Daniel Graf
 */
@ApplicationScoped
public class ObjectIdConverter {
    private static final Logger log = Logger.getLogger(ObjectIdConverter.class);

    public static ObjectId fromString(String id) {
        // check for hexadecimal String with length of 24
        if (!id.matches("[0-9a-fA-F]{24}")) {
            String errMsg = "ID: " + id + " can't be passed into ObjectID - not a 24 byte hexadecimal string [0-9a-FA-F]";
            log.debug(errMsg);
            // throw new IllegalArgumentException(errMsg);
            throw new ValidationException(errMsg);
        }
        return new ObjectId(id);
    }
}
