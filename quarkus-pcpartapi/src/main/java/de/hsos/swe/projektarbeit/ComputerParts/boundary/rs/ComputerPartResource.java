package de.hsos.swe.projektarbeit.ComputerParts.boundary.rs;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CaseDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.CpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.FanDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.GpuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.MotherboardDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.PsuDTO;
import de.hsos.swe.projektarbeit.ComputerParts.boundary.dto.RamDTO;
import de.hsos.swe.projektarbeit.ComputerParts.control.ComputerPartCatalog;
import de.hsos.swe.projektarbeit.ComputerParts.control.PartDTOConverter;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * =====================================================
 * | Computer Parts REST-Resource (by type)
 * =====================================================
 *
 * @author Jannis Welkener (main-author)
 * @author Daniel Graf (co-author), added:
 *          - OpenAPI documentation
 */
@Path("/pc-parts/type")
@Tag(name = "Computer Parts by Type", description = "List and view computer parts by various types or add new ones (elevated permissions needed)")
public class ComputerPartResource {

    @Inject
    ComputerPartCatalog repository;

    @GET
    @PermitAll
    @Path("gpus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGPUs() {
        return Response.status(Status.OK).entity(repository.getPartByType(GPU.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("gpus")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGPU(GpuDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("fans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFans() {
        return Response.status(Status.OK).entity(repository.getPartByType(Fan.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("fans")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addFan(FanDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("cases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCases() {
        return Response.status(Status.OK).entity(repository.getPartByType(Case.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("cases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCase(CaseDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("psus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPSUs() {
        return Response.status(Status.OK).entity(repository.getPartByType(PSU.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("psus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPSU(PsuDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("motherboards")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMotherboards() {
        return Response.status(Status.OK).entity(repository.getPartByType(Motherboard.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("motherboards")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMotherboard(MotherboardDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("cpus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCPUs() {
        return Response.status(Status.OK).entity(repository.getPartByType(CPU.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("cpus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCPU(CpuDTO dto) {
        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));
        return Response.status(Status.OK).entity("Saved").build();
    }

    @GET
    @PermitAll
    @Path("rams")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRAMs() {
        return Response.status(Status.OK).entity(repository.getPartByType(RAM.class)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("rams")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRAM(@Valid RamDTO dto) {

        this.repository.saveToDB(PartDTOConverter.dtoConverter(dto));

        return Response.status(Status.OK).entity("Saved").build();
    }
    /*
     *
     * /*
     * TODO
     * POST,DELETE
     * RBAC
     *
     * IMG -> HATEOAS (.../gpu/{id}/img)
     */

}
