package de.hsos.swe.projektarbeit.Reviews.boundary.rs;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.Reviews.boundary.dto.CommentInputDTO;
import de.hsos.swe.projektarbeit.Reviews.control.PartCommentsService;
import de.hsos.swe.projektarbeit.Reviews.gateway.dto.CommentOutputDTO;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.UUID;

/**
 * @author Daniel Graf
 */
@Path("/pc-parts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comments & Ratings for Computer Parts", description = "Write and look at ratings for computer parts")
public class PartCommentResource {
    private static final Logger log = Logger.getLogger(ComputerCommentsResource.class);

    @Inject
    PartCommentsService repository;

    /**
     * =====================================================
     * | GET Retrieve Part Comments, by id
     * =====================================================
     *
     * @author Daniel Graf
     */
    @GET
    @Path("/{partId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get comments for a computer part")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getPartComments(@PathParam("partId") @Pattern(regexp = "[0-9a-fA-F]{24}") String partId) {

        List<CommentOutputDTO> comments = this.repository.getPartComments(partId);
        return Response.status(Status.OK).entity(comments).build();
    }

    /**
     * =====================================================
     * | POST Add Comment to Part by id
     * =====================================================
     *
     * @author Daniel Graf
     */
    @POST
    @Authenticated
    @Path("/{partId}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Write and publish a comment for a computer part")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "201", description = "Comment created")
    @APIResponse(responseCode = "401", description = "No rights to post comment")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response writePartComment(@Context SecurityContext securityContext,
            @PathParam("partId") @Pattern(regexp = "[0-9a-fA-F]{24}") String partId,
            @Valid CommentInputDTO comment) {
        CommentOutputDTO createdComment = this.repository.publishPartComment(partId, comment.commentary,
                securityContext.getUserPrincipal().getName(), comment.userRating);
        return Response.status(Status.CREATED).entity(createdComment).build();
    }

    /**
     * =====================================================
     * | DELETE Comment from Part by id
     * =====================================================
     *
     * @author Daniel Graf
     */
    @DELETE
    @Authenticated
    @Path("/{partId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "[Elevated rights] Delete a comment from a computer part")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "201", description = "Comment created")
    @APIResponse(responseCode = "401", description = "No rights to post comment")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response deletePartComment(@Context SecurityContext securityContext,
            @PathParam("commentId") @Pattern(regexp = "[0-9a-fA-F]{24}") String commentId,
            @PathParam("partId") @Pattern(regexp = "[0-9a-fA-F]{24}") String partId) {

        this.repository.deletePartComment(partId, commentId);
        return Response.status(Status.OK).build();
    }
}
