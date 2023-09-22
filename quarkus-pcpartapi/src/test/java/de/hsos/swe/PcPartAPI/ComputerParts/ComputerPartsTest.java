package de.hsos.swe.PcPartAPI.ComputerParts;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsos.swe.projektarbeit.Users.gateway.UserRepository;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * @author Daniel Graf
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComputerPartsTest {
    private static final Logger log = Logger.getLogger(ComputerPartsTest.class);

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = { "admin" })
    void shouldGetComponents() {
        Response response = given()
                .get("/pc-parts/all-parts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertTrue(response.getBody().asString().contains("partName"));
    }

}
