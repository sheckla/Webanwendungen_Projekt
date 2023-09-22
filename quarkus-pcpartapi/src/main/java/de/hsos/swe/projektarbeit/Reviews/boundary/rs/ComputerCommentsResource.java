package de.hsos.swe.projektarbeit.Reviews.boundary.rs;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.Reviews.boundary.dto.CommentInputDTO;
import de.hsos.swe.projektarbeit.Reviews.control.ComputerCommentsService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.UnavailableException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Jannis Welkener
 * @author Daniel Graf (co-author), added:
 *         - OpenAPI documentation
 *         - Fault tolerance
 *         - input validation
 */
@Tag(name = "Comments & Ratings for Computer Configurations", description = "View and create comments and ratings for public computer configurations")
@Path("/computers")
@RequestScoped
public class ComputerCommentsResource {
    private static final Logger log = Logger.getLogger(ComputerCommentsResource.class);

    @Inject
    ComputerCommentsService repository;

    /**
     * =====================================================
     * | GET Computer Configuration Comment
     * =====================================================
     *
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     */
    @GET
    @Path("/{computerID}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get comments for a computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getComments(@PathParam("computerID") @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        return Response.status(Status.OK).entity(repository.getComputerComments(id)).build();
    }

    /**
     * =====================================================
     * | POST Computer Configuration Comment
     * =====================================================
     *
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     */
    @POST
    @Authenticated
    @Path("computers/{computerID}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Write a comment for a computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response writeComment(@PathParam("computerID") @Pattern(regexp = "[0-9a-fA-F]{24}") String computerID,
            @Context SecurityContext securityContext, @Valid CommentInputDTO comment) throws UnavailableException {
        this.repository.publicComputerComment(computerID, comment.commentary,
                securityContext.getUserPrincipal().getName(),
                comment.userRating);

        return Response.status(Status.OK).entity(comment).build();
    }

    /**
     * =====================================================
     * | DELETE Comment (Computer Configuration)
     * =====================================================
     *
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *             - input validation
     */
    @DELETE
    @RolesAllowed("moderator")
    @Path("/{computerID}/comments/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "(moderator privileges needed) Delete a comment from a computer")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response deleteComment(@PathParam("computerID") @Pattern(regexp = "[0-9a-fA-F]{24}") String pcId,
            @PathParam("commentID") @Pattern(regexp = "[0-9a-fA-F]{24}") String commentId) {

        this.repository.deleteComputerComment(pcId, commentId);
        return Response.status(Status.OK).build();
    }

}
