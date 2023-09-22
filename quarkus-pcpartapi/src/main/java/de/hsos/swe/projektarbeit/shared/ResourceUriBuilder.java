package de.hsos.swe.projektarbeit.shared;

import java.net.URI;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

/**
 * @author Daniel Graf
 */
@ApplicationScoped
public class ResourceUriBuilder {

    // public URI forPersonById(String personId, UriInfo uriInfo, String path) {
    // return createResourceUri(PlayerResource.class, personId, uriInfo, path);
    // }

    // public URI forTest(String id, UriInfo info, String path) {
    // return this.createResourceUri(getClass(), id, info, path);
    // }

    public URI createResourceUri(Class<?> resourceClass, String id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(String.valueOf(id)).build(id);
    }

    public URI createResourceUri(Class<?> resourceClass, String id, UriInfo uriInfo, String path) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(String.valueOf(id)).path(path).build(id);
    }

}
