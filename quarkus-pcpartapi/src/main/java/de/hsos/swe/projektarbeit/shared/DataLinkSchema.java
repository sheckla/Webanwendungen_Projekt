package de.hsos.swe.projektarbeit.shared;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Graf
 */
public class DataLinkSchema {
    public Object data;
    private Map<String, URI> links;
    private Map<String, Map<String, URI>> relationships;


    public void addLink(String linkName, URI uri) {
        if (this.links == null) {
            this.links = new HashMap<>();
        }
        this.links.put(linkName, uri);
    }

    public void addRelationship(String relationshipName, String linkName, URI linkUri) {
        if (this.relationships == null) {
            this.relationships = new HashMap<String, Map<String, URI>>();
        }

        if (!this.relationships.containsKey(relationshipName)) {
            this.relationships.put(relationshipName, new HashMap<String, URI>());
        }

        this.relationships.get(relationshipName).put(linkName, linkUri);
    }


    public Map<String, URI> getLinks() {
        return links;
    }

    public Map<String, Map<String, URI>> getRelationships() {
        return relationships;
    }
}
