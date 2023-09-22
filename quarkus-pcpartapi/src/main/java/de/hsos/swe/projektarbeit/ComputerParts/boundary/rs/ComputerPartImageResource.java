package de.hsos.swe.projektarbeit.ComputerParts.boundary.rs;

import java.io.IOException;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartImageService;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.shared.MultipartBody;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * =====================================================
 * | Computer Part Image REST-Resource
 * =====================================================
 *
 * @author Jannis Welkener
 * @author Daniel Graf, added:
 *         - OpenAPI documentation
 *         - input validation
 */
@Path("/pc-parts")
@Tag(name = "Computer Part Images", description =  "Upload and look at the images of computer parts")
public class ComputerPartImageResource {

    @Inject
    ComputerPartCatalog repository;

    @Inject
    ComputerPartImageService imageRepository;

    @GET
    @Path("{id}/image")
    @Produces("image/png")
    @Operation(summary = "Get the image of a computer part by id")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @APIResponse(responseCode = "201", description = "Image retrieved")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response getImg(@PathParam("id") @Pattern(regexp = "[0-9a-fA-F]{24}") String partID) {

        if (repository.getPartByID(partID).getPartImage() == null) {
            return Response.ok("No image").build();
        }

        try {
            return Response.ok(this.imageRepository.getImage(partID)).build();
        } catch (IOException e) {
            return Response.ok(e).build();
        }
    }

    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("{id}/image")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Upload an image for a computer part")
    @APIResponse(responseCode = "400", description = "Invalid ID")
    @APIResponse(responseCode = "201", description = "Image uploaded")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)
    public Response uploadImg(@MultipartForm MultipartBody data,
            @PathParam("id") @Pattern(regexp = "[0-9a-fA-F]{24}") String partID) {
        return Response.ok(imageRepository.setImage(partID, data.getFile())).build();
    }

}
