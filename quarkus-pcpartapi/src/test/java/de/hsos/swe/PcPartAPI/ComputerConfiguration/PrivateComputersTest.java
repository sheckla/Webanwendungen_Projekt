package de.hsos.swe.PcPartAPI.ComputerConfiguration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.rs.PrivateComputersResource;
import de.hsos.swe.projektarbeit.ComputerConfiguration.entity.Computer;
import de.hsos.swe.projektarbeit.shared.DataLinkSchema;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.stream.JsonParserFactory;
import jakarta.ws.rs.core.Response.Status;

/**
 * =====================================================
 * BLACK BOX Test, test for specified use-cases
 * =====================================================
 *
 * @author Daniel Graf
 */

@QuarkusTest
@TestHTTPEndpoint(PrivateComputersResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivateComputersTest {
    private static final Logger log = Logger.getLogger(PrivateComputersTest.class);
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    @ConfigProperty(name = "pcpart.api.testing.credentials.admin.username")
    String username;

    @Inject
    @ConfigProperty(name = "pcpart.api.testing.credentials.admin.password")
    String password;

    @Test
    @Order(1)
    public void shouldNotCreatePrivateComputerWhenAnonymous() {
        String newConfigurationName = "MyPc1";
        Response res = given()
                .contentType(ContentType.JSON)
                .body(newConfigurationName).put("/create-new-pc");
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), res.statusCode());
    }

    @Test
    @Order(2)
    @TestSecurity(user = "user", roles = { "user" })
    public void shouldCreatePrivateComputerWhenAuthenticated() {
        String newConfigurationName = "MyPc1";

        Response response = given().contentType(ContentType.JSON).body(newConfigurationName).when()
                .put("/create-new-pc").then().statusCode(Status.CREATED.getStatusCode()).extract().response();

        assertEquals(Status.CREATED.getStatusCode(), response.getStatusCode());
    }

}
