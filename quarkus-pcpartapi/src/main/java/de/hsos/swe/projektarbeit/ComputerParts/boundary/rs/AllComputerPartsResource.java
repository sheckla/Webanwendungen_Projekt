package de.hsos.swe.projektarbeit.ComputerParts.boundary.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.rs.PrivateComputersResource;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CaseDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.FanDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.GpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.MotherboardDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.PsuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.RamDTO;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.ComputerParts.control.PartDTOConverter;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PartType;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PcPart;
import de.hsos.swe.projektarbeit.ComputerParts.gateway.PartRepository;
import de.hsos.swe.projektarbeit.Reviews.boundary.rs.ComputerCommentsResource;
import de.hsos.swe.projektarbeit.shared.DataLinkSchema;
import de.hsos.swe.projektarbeit.shared.ResourceUriBuilder;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;

/**
 * =====================================================
 * | Computer Parts REST-Resource
 * =====================================================
 *
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *         - OpenAPI documentation
 */
@Path("/pc-parts")
@PermitAll
@Tag(name = "All Computer Parts", description = "List and view various computer parts")
@RequestScoped
public class AllComputerPartsResource {
    private static final Logger log = Logger.getLogger(AllComputerPartsResource.class);

    @Inject
    ComputerPartCatalog repository;

    @Inject
    UriInfo uriInfo;
    @Inject
    ResourceUriBuilder uriBuilder;

    /**
     * @author Jannis Welkener (main-author)
     * @author Daniel Graf (co-author), added:
     *         - Query parameter
     *         - query filtering
     *         - performance measurement
     *         - logging
     */
    // TODO page validation
    @GET
    @PermitAll
    @Path("all-parts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve all computer components")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    @Timeout(value = 2500)
    @CircuitBreaker(requestVolumeThreshold = 10)

    public Response getAllParts(
            @QueryParam("page") @PositiveOrZero int page,
            @QueryParam("pageSize") @PositiveOrZero int pageSize,
            @QueryParam("partNameFilter") String partNameFilter,
            @QueryParam("hasImageFilter") Boolean hasImageFilter,
            @QueryParam("typeFilter") PartType typeFilter,
            @QueryParam("manufacturerNameFilter") String manufacturerNameFilter) {
        List<PcPart> parts = repository.getAllPartsFromDB();
        log.info("Getting all parts");

        if (page > 0 && pageSize > 0) {
            // Apply pagination
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, parts.size());
            if (startIndex > parts.size()) {
                log.info("Startindex exceeds total part amount");
                return Response.status(Status.BAD_REQUEST).build();
            }
            parts = parts.subList(startIndex, endIndex);
        }

        /*****************************
         * Filter Start
         *****************************/
        long startTime = System.nanoTime();
        // PartName filtering
        if (partNameFilter != null && !partNameFilter.isEmpty()) {
            log.info("Filtering by partName");
            parts = AllComputerPartsResource.filterByPartName(parts, partNameFilter);
        }

        // Type Filtering
        if (typeFilter != null) {
            log.info("Filtering by part type");
            parts = filterByType(parts, typeFilter);
        }

        // Manufacturer name filtering
        if (manufacturerNameFilter != null && !manufacturerNameFilter.isEmpty()) {
            log.info("Filtering by manufacturer name");
            parts = filterByManufacturerName(parts, manufacturerNameFilter);
        }
        long endTime = System.nanoTime();
        long finalTime = endTime - startTime;
        double benchmarkTime = (double) finalTime / 1000 / 1000;
        log.info("Filtering done! took " + (benchmarkTime) + "ms");

        ArrayList<DataLinkSchema> schemas = new ArrayList<>();
        parts.forEach(part -> {
            DataLinkSchema schema = new DataLinkSchema();
            schema.data = part;
            schema = this.getPartWithSelfLink(part, schema);
            if (part.hasImage) {

                schema = this.getPartImageRelationship(part, schema);
            }
            schemas.add(schema);
        });

        return Response.status(Status.OK).entity(schemas).build();
    }

    /**
     * @author Jannis Welkener
     */
    @GET
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a computer part by id")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    public Response getAllParts(@PathParam("id") @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        DataLinkSchema schema = new DataLinkSchema();
        PcPart part = this.repository.getPartByID(id);
        schema.data = part;
        schema = this.getPartWithSelfLink(part, schema);
        if (part.hasImage) {
            schema = this.getPartImageRelationship(part, schema);
        }
        return Response.status(Status.OK).entity(schema).build();
    }

    @DELETE
    @PermitAll
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete by id")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "200", description = "Success")
    public Response deletePart(@PathParam("id") @Pattern(regexp = "[0-9a-fA-F]{24}") String id) {
        DataLinkSchema schema = new DataLinkSchema();
        PcPart part = this.repository.getPartByID(id);
        if (part == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        this.repository.deleteById(id);
        return Response.status(Status.OK).entity(schema).build();
    }

    /**
     * @author Jannis Welkener
     */
    @PUT
    @RolesAllowed("admin")
    @Path("{type}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a new computer part")
    @APIResponse(responseCode = "400", description = "Invalid Parameter")
    @APIResponse(responseCode = "201", description = "Success")
    public Response addPart(@PathParam("type") String type,
            @RequestBody(description = "Choose ONE of these Schemas", required = true, content = @Content(schema = @Schema(properties = {
                    @SchemaProperty(name = "cpu", implementation = CpuDTO.class),
                    @SchemaProperty(name = "gpu", implementation = GpuDTO.class),
                    @SchemaProperty(name = "case", implementation = CaseDTO.class),
                    @SchemaProperty(name = "fan", implementation = FanDTO.class),
                    @SchemaProperty(name = "motherboard", implementation = MotherboardDTO.class),
                    @SchemaProperty(name = "psu", implementation = PsuDTO.class),
                    @SchemaProperty(name = "ram", implementation = RamDTO.class)
            }))) String json) {

        boolean success = false;
                System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            switch (type.toLowerCase()) {
                case "gpu":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<GpuDTO>() {
                            })));
                    success = true;
                    break;
                case "cpu":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<CpuDTO>() {
                            })));
                    success = true;
                    break;
                case "case":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<CaseDTO>() {
                            })));
                    success = true;
                    break;
                case "fan":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<FanDTO>() {
                            })));
                    success = true;
                    break;
                case "motherboard":
                    this.repository.saveToDB(PartDTOConverter
                            .dtoConverter(objectMapper.readValue(json, new TypeReference<MotherboardDTO>() {
                            })));
                    success = true;
                    break;
                case "psu":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<PsuDTO>() {
                            })));
                    success = true;
                    break;
                case "ram":
                    this.repository.saveToDB(
                            PartDTOConverter.dtoConverter(objectMapper.readValue(json, new TypeReference<RamDTO>() {
                            })));
                    success = true;
                    break;
            }

            if (success) {
                return Response.status(Status.OK).entity(type + " added").build();
            }

            return Response.status(Status.OK).entity(type + " - Not a valid type").build();

        } catch (Exception e) {
            return Response.status(Status.OK).entity("Invalid JSON").build();
        }
    }

    /**
     * @author Daniel Graf
     */
    public static List<PcPart> filterByPartName(List<PcPart> parts, String partName) {

        return parts.stream()
                .filter(part -> part.getPartName().toLowerCase().contains(partName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * @author Daniel Graf
     */
    public static List<PcPart> filterByType(List<PcPart> parts, PartType type) {
        return parts.stream()
                .filter(part -> part.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * @author Daniel Grafw
     */
    private List<PcPart> filterByManufacturerName(List<PcPart> parts, String manufacturerName) {
        return parts.stream()
                .filter(part -> part.getManufacturerName().toLowerCase().contains(manufacturerName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema getPartWithSelfLink(PcPart part, DataLinkSchema dto) {
        dto.addLink("self",
                this.uriBuilder.createResourceUri(AllComputerPartsResource.class, part.id.toString(), this.uriInfo));
        return dto;
    }

    /**
     * @author Daniel Graf
     */
    private DataLinkSchema getPartImageRelationship(PcPart part, DataLinkSchema dto) {
        dto.addRelationship("image", "related",
                this.uriBuilder.createResourceUri(AllComputerPartsResource.class, part.id.toString(), this.uriInfo,
                        "/image"));
        return dto;
    }
}
