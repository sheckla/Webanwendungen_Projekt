package de.hsos.swe.projektarbeit.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerConfiguration.gateway.ComputerRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * @author Daniel Graf
 * TODO: remove and make startup script for mongodb => docker
 */
@ApplicationScoped
public class MongoDBLoader {
    private final static Logger log = Logger.getLogger("org.mongodb.driver");

    // TODO find better way to turn of logging for MongoDB Driver...
    public void onStart(@Observes StartupEvent ev) {
        log.info("MongoDB Driver logging = OFF");
        log.setLevel(Level.OFF);
    }

}
