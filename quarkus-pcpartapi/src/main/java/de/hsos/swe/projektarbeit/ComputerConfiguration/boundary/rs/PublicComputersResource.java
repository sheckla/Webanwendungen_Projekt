package de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.rs;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.hsos.swe.projektarbeit.ComputerConfiguration.control.PublicComputerService;
import de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto.ComputerDTOReduced;
import de.hsos.swe.projektarbeit.Reviews.boundary.rs.ComputerCommentsResource;
import de.hsos.swe.projektarbeit.shared.DataLinkSchema;
import de.hsos.swe.projektarbeit.shared.ResourceUriBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;

/**
 * =====================================================
 * | Private Computer Configurations REST-Resource
 * =====================================================
 *
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *         - OpenAPI documentation
 *         - fault tolerance
 *         - input validation
 */
@RequestScoped
@Path("/computers/public")
@Tag(name = "Public Computer Configurations", description = "List and view all public listed computer configurations sourced by various users on this platform")

public class PublicComputersResource {

    @Inject
    PublicComputerService publicComputerService;

    @Inject
    ResourceUriBuilder uriBuilder;

    @Context
    UriInfo uriInfo;

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - HATEOAS
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve all public computer configurations")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getAllPublicPCs() {
        List<ComputerDTOReduced> computerDTOs = this.publicComputerService.getAllPublicComputersAsDTO();
        ArrayList<DataLinkSchema> dataLinkSchemaDTOs = new ArrayList<>();
        computerDTOs.forEach(computerDTO -> {
            DataLinkSchema dls = new DataLinkSchema();
            dls.data = computerDTO;
            dls = this.attachPublicComputerSelfLinkUri(computerDTO);
            if (computerDTO.commentAmount > 0) {
                dls = this.attachPublicComputerCommentLinkUri(computerDTO, dls);
            }
            dataLinkSchemaDTOs.add(dls);
        });

        return Response.status(Status.OK).entity(dataLinkSchemaDTOs).build();
    }

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - HATEOAS
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Retrieve a specific public computer configuration by id")
    @APIResponse(responseCode = "400", description = "Invalid Input")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getPublicPC(@PathParam("id") @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        return Response.status(Status.OK).entity(publicComputerService.getPublicPcByID(id)).build();
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema attachPublicComputerSelfLinkUri(ComputerDTOReduced computerDTO) {
        DataLinkSchema dataLink = new DataLinkSchema();
        dataLink.data = computerDTO;

        dataLink.addLink("self",
                this.uriBuilder.createResourceUri(PublicComputersResource.class, computerDTO.id, this.uriInfo));
        return dataLink;
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema attachPublicComputerCommentLinkUri(ComputerDTOReduced computerDTO, DataLinkSchema dataLink) {
        dataLink.addRelationship("comments", "related",
                this.uriBuilder.createResourceUri(ComputerCommentsResource.class, computerDTO.id, this.uriInfo,
                        "/comments"));
        return dataLink;
    }
}
