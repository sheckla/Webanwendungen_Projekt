package de.hsos.swe.projektarbeit.Users.boundary.rs;

import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.Users.boundary.dto.UserDTO;
import de.hsos.swe.projektarbeit.Users.control.UserCatalog;
import de.hsos.swe.projektarbeit.Users.entity.User;
import de.hsos.swe.projektarbeit.Users.gateway.dto.UserInfoDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Jannis Welkener
 * @author Daniel Graf, added:
 *         - OpenAPI documentation
 *         - input validation
 *         - fault tolerance
 */
@Path("users")
@Authenticated
@Transactional
@Tag(name = "User Account", description = "Register and login to your created user account")

public class UsersResource {
    private static final Logger LOG = Logger.getLogger(UsersResource.class);

    @Inject
    private UserCatalog repository;

    @GET
    @Path("/me")
    @RolesAllowed({ "user", "admin", "moderator" })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve your personal account info (requires login first)")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "Account information has been retrieved")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response me(@Context SecurityContext securityContext) {
        // User user = this.repository.
        if (this.repository.usernameNotAlreadyTaken(securityContext.getUserPrincipal().getName())) {
            System.out.println("username not already taken");
        }
        UserInfoDTO dto = this.repository.findById(securityContext.getUserPrincipal().getName());
        return Response.status(200).entity(dto).build();
        // return Response
        // return
        // Response.status(200).entity(securityContext.getUserPrincipal().getName()).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register a new user")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "User has been registered")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response registerUser(@Valid UserDTO credentials) {

        if (repository.usernameNotAlreadyTaken(credentials.name)) {

            LOG.info("User registered");
            repository.register(credentials.name, credentials.password, "user");
            return Response.status(200).entity("User registered").build();

        } else {
            return Response.status(400).entity("Username already exists, please choose a different one").build();
        }
    }

    @POST
    @Path("/admin-commands/register-moderator")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register a moderator account (admin privileges needed)")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "Account information has been retrieved")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response registerModerator(@Valid UserDTO credentials) {

        if (repository.usernameNotAlreadyTaken(credentials.name)) {

            repository.register(credentials.name, credentials.password, "moderator");
            return Response.status(200).entity("Moderator registered").build();

        } else {
            return Response.status(400).entity("Username already exists, please choose a different one").build();
        }
    }

    /*
     * @POST
     *
     * @Path("/changePassword")
     *
     * @RolesAllowed({"admin","user","moderator"})
     *
     * @Consumes(MediaType.APPLICATION_JSON)
     *
     * @Produces(MediaType.TEXT_PLAIN)
     * public Response changePassword(@Context SecurityContext securityContext,
     * String newPassword) {
     *
     * //securityContext.;
     * //repository.changePassword(newPassword);
     *
     * return Response.status(200).entity("Password changed").build();
     * }
     */

    @GET
    @Path("/admin-commands/get-users")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve all user-accounts (admin privileges needed)")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "Users have been retrieved")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getUsers() {
        return Response.status(200).entity(repository.getAllUsers()).build();
    }
}
