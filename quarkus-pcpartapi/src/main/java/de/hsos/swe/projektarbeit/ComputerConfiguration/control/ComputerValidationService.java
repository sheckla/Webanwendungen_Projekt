package de.hsos.swe.projektarbeit.ComputerConfiguration.control;

import jakarta.validation.ValidationException;

/**
 * Encapsulates availability checking to this interface
 * @author Daniel Graf
 */
public interface ComputerValidationService {
    public boolean computerConfigurationExists(String pcId) throws ValidationException;
    public boolean computerConfigurationIsPublic(String pcId) throws ValidationException;
}
