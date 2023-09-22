package de.hsos.swe.projektarbeit.shared;

import java.io.InputStream;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
/**
 * @author Jannis Welkener
 */
public class MultipartBody {

    @FormParam("image-file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream file;

    public InputStream getFile() {
        return file;
    }

}
