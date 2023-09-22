package de.hsos.swe.projektarbeit.Reviews.control.acl;

import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerConfiguration.control.ComputerValidationService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.gateway.ComputerRepository;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.NotFoundException;

/**
 * @author Daniel Graf
 */
@ApplicationScoped
public class ComputerLookupAdapter implements ComputerLookupService {
    private static final Logger log = Logger.getLogger(ComputerLookupAdapter.class);
    private ComputerValidationService computerValidationService = new ComputerRepository();

    @Override
    public boolean checkIfComputerExists(String pcId) throws NotFoundException {
        boolean exists = this.computerValidationService.computerConfigurationExists(pcId);
        if (!exists) {
            throw new NotFoundException("Computer lookup: doesn't exist");
        }
        return exists;
    }

    @Override
    public boolean checkIfComputerIsPublic(String pcId)
    throws NotFoundException, UnauthorizedException {
        this.checkIfComputerExists(pcId);
        boolean isPublic = this.computerValidationService.computerConfigurationIsPublic(pcId);
        if (!isPublic) {
            throw new UnauthorizedException("Computer lookup: is not public");
        }
        return isPublic;
    }

}
