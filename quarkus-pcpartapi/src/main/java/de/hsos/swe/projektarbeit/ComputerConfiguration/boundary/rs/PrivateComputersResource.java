package de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.rs;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.ComputerUpdateInputDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto.VisibilityInputDTO;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.PrivateComputerService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerDTOReduced;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerStatusResponse;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;
import de.hsos.swe.projektarbeit.Reviews.boundary.rs.ComputerCommentsResource;
import de.hsos.swe.projektarbeit.Reviews.boundary.rs.PartCommentResource;
import de.hsos.swe.projektarbeit.shared.DataLinkSchema;
import de.hsos.swe.projektarbeit.shared.ObjectIdConverter;
import de.hsos.swe.projektarbeit.shared.ResourceUriBuilder;
import io.quarkus.security.Authenticated;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

/**
 * =====================================================
 * | Private Computer Configurations REST-Resource
 * =====================================================
 *
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *         - OpenAPI documentation
 *         - fault tolerance
 */
@RequestScoped
@Path("/computers/private")
@Tag(name = "Private Computer Configurations", description = "Create and configure your personal computer configurations")
public class PrivateComputersResource {
    private static final Logger log = Logger.getLogger(PrivateComputersResource.class);

    @Inject
    PrivateComputerService repository;

    @Context
    UriInfo uriInfo;

    @Inject
    ResourceUriBuilder uriBuilder;

    @ConfigProperty(name = "pcpart.api.experimental.enabled", defaultValue = "false")
    boolean addPcCompleteEnabled;

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author),
     *         - HATEOAS
     */
    @PUT
    @Authenticated
    @Path("/create-new-pc")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new empty computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @APIResponse(responseCode = "201", description = "New Computer Configuration has been created")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response addPC(@Context SecurityContext securityContext,
            @Schema(defaultValue = "AwesomeNewComputerName") @NotBlank String pcName,
            @Valid String id) {
        ComputerDTOReduced computerDTO = this.repository.createNewPC(securityContext.getUserPrincipal().getName(),
                pcName);

        DataLinkSchema dataLink = getComputerWithSelfLink(computerDTO);

        return Response.status(Status.CREATED)
                .entity(dataLink).build();
    }

    /**
     * @author Daniel Graf
     */
    @PUT
    @Authenticated
    @Path("/create-new-pc/pre-filled")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "[DEV] Creates a new computer configuration with randomly selected parts")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @APIResponse(responseCode = "201", description = "New Computer Configuration has been created")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response addPcComplete(@Context SecurityContext securityContext,
            @Schema(defaultValue = "AwesomeNewComputerName") @NotBlank String pcName) {
        ComputerDTOReduced computerDTO = this.repository.createNewPcRandom(securityContext.getUserPrincipal().getName(),
                pcName);

        DataLinkSchema dataLink = getComputerWithSelfLink(computerDTO);

        return Response.status(Status.CREATED)
                .entity(dataLink).build();
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     *         - response messages
     */
    @POST
    @Authenticated
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add or update parts of the computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response updatePC(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String id,
            @Valid ComputerUpdateInputDTO computerUpdateDTO) {

        ObjectId pcObjectId;
        ComputerStatusResponse response = new ComputerStatusResponse();
        try {
            pcObjectId = ObjectIdConverter.fromString(id);
            response = this.repository.setPrivateComputerComponentsById(securityContext.getUserPrincipal().getName(),
                    pcObjectId,
                    computerUpdateDTO);
        } catch (IllegalArgumentException err) {
            return Response.status(Status.BAD_REQUEST).entity(err.toString()).build();
        }
        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     *         - response messages
     */
    @POST
    @Authenticated
    @Path("/{id}/pc-parts/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add or update parts of the computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response updateComputerComponent(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String pcId,
            @PathParam("type") PartType type, @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String partId) {

        // Check for valid id
        ObjectId pcObjectId;
        try {
            pcObjectId = ObjectIdConverter.fromString(pcId);
        } catch (IllegalArgumentException err) {
            log.info(err.toString());
            return Response.status(Status.BAD_REQUEST).entity(err.toString()).build();
        }

        ComputerStatusResponse response = this.repository
                .setPrivateComputerComponentByPartType(securityContext.getUserPrincipal().getName(), pcObjectId, partId,
                        type);
        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * Delete Computer Configuration Part - specific, by Type
     *
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input validation
     *         - response messages
     */
    @DELETE
    @Authenticated
    @Path("/{id}/pc-parts/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete parts of a computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response deleteComputerComponent(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String computerId,
            @PathParam("type") @Valid PartType type) {
        ObjectId computerObjectId;
        try {
            computerObjectId = ObjectIdConverter.fromString(computerId);
        } catch (IllegalArgumentException exception) {
            log.info(exception.toString());
            return Response.status(Status.BAD_REQUEST).build();
        }
        ComputerStatusResponse response = this.repository
                .deletePrivateComputerComponentByPartType(securityContext.getUserPrincipal().getName(),
                        computerObjectId, type);
        return Response.status(response.code).entity(response).build();
    }

    /**
     * Set Computer Configuration Visibility, public || private
     *
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - input DTO
     *         - input validation
     *         - response messages
     */
    @POST
    @Authenticated
    @Path("/{id}/visibility")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Set to 'public' or 'private' to change if other users can see the computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response setPCtoPublic(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String id,
            @Valid VisibilityInputDTO visibility) {
        ObjectId pcObjectId;
        try {
            pcObjectId = ObjectIdConverter.fromString(id);
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
        }
        String username = securityContext.getUserPrincipal().getName();
        ComputerStatusResponse response = new ComputerStatusResponse();
        if (visibility.isPublic) {
            response = this.repository.publishPC(username, pcObjectId);
        } else {
            response = this.repository.hideComputer(username, pcObjectId);
        }

        return Response.status(response.code).entity(response).build();
    }

    /**
     * List all private Configurations
     *
     * @author Jannis Welkener
     * @author Daniel Graf, additionally:
     *         - HATEOAS
     */
    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List all private computer configurations")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getAllPrivatePCs(@Context SecurityContext securityContext) {
        long startTime = System.nanoTime();

        List<ComputerDTOReduced> computers = this.repository
                .getAllPrivateComputersAsDTO(securityContext.getUserPrincipal().getName());

        List<DataLinkSchema> dataLinkDTOs = new ArrayList<>();

        computers.forEach(cDTO -> {
            DataLinkSchema dataLinkDTO = this.getComputerWithSelfLink(cDTO);
            if (cDTO.isPublic) {
                dataLinkDTO = this.attachPublicComputerRelatedLink(cDTO, dataLinkDTO);
            }
            if (cDTO.commentAmount > 0) {
                dataLinkDTO = this.attachCommenDataLinkDTO(cDTO, dataLinkDTO);
            }
            dataLinkDTOs.add(dataLinkDTO);
        });
        long endTime = System.nanoTime();
        long finalTime = endTime - startTime;
        double benchmarkTime = (double) finalTime / 1000 / 1000;
        log.info("Listing all private computer configurations done! took " + (benchmarkTime) + "ms");
        return Response.status(Status.OK).entity(dataLinkDTOs).build();
    }

    /**
     * Delete Private Computer Configuration
     *
     * @author Daniel Graf (co-author), added:
     *         - input DTO
     *         - input validation
     *         - response messages
     */
    @DELETE
    @Authenticated
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete a private computer configuration permanently")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response deletePrivateComputerConfiguration(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        ObjectId partObjectId;
        try {
            partObjectId = ObjectIdConverter.fromString(id);
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        ComputerStatusResponse response = this.repository
                .deletePrivateComputerConfiguration(securityContext.getUserPrincipal().getName(), partObjectId);
        return Response.status(Status.OK)
                .entity(response).build();
    }

    /**
     * Show Computer Configuration - specific, via ID
     *
     * @author Daniel Graf (co-author), added:
     *         - input validation
     *         - response messages
     */
    @GET
    @Authenticated
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Show Information of a specific private computer configuration")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getPrivatePC(@Context SecurityContext securityContext,
            @PathParam("id") @Valid @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        // check for valid id-string
        try {
            ObjectId objectId = ObjectIdConverter.fromString(id);
        } catch (IllegalArgumentException err) {
            return Response.status(Status.BAD_REQUEST).entity(err.toString()).build();
        }
        return Response.status(Status.OK)
                .entity(repository.getPrivatePcByID(securityContext.getUserPrincipal().getName(),
                        id))
                .build();
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema getComputerWithSelfLink(ComputerDTOReduced computerDTO) {
        DataLinkSchema dataLink = new DataLinkSchema();
        dataLink.data = computerDTO;

        dataLink.addLink("self",
                this.uriBuilder.createResourceUri(PrivateComputersResource.class, computerDTO.id, this.uriInfo));
        return dataLink;
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema attachPublicComputerRelatedLink(ComputerDTOReduced computerDTO,
            DataLinkSchema dataLink) {
        dataLink.addLink("related",
                this.uriBuilder.createResourceUri(PublicComputersResource.class, computerDTO.id, this.uriInfo));
        return dataLink;
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema attachCommenDataLinkDTO(ComputerDTOReduced computerDTO, DataLinkSchema dataLink) {
        dataLink.addRelationship("comments", "related",
                this.uriBuilder.createResourceUri(ComputerCommentsResource.class, computerDTO.id, this.uriInfo,
                        "/comments"));
        return dataLink;
    }
}
