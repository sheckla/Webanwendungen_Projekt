package de.hsos.swe.projektarbeit.ComputerParts.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jannis Welkener
 */
public interface ComputerPartImageService {
    String setImage(String id,InputStream stream);
    BufferedImage getImage(String partID)throws IOException;
}
