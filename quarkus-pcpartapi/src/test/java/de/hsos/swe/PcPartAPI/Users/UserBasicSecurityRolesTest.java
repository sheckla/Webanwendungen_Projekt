package de.hsos.swe.PcPartAPI.Users;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsos.swe.projektarbeit.Users.boundary.rs.UsersResource;
import de.hsos.swe.projektarbeit.Users.gateway.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Daniel Graf
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserBasicSecurityRolesTest {
    private static final Logger log = Logger.getLogger(UserBasicSecurityRolesTest.class);

    @Test
    @Order(1)
    void shouldAccessPublicWhenAnonymous() {
        get("/computers/public")
        .then()
        .statusCode(Status.OK.getStatusCode());

    }

    @Test
    @Order(2)
    void shouldNotAccessAdminWhenAnonymous() {
        get("/users/admin-commands/get-users")
        .then()
        .statusCode(Status.UNAUTHORIZED.getStatusCode());

    }

    @Test
    @Order(3)
    @TestSecurity(user = "moderator", roles = { "moderator" })
    void shouldNotAccessAdinWithMissingPrivileges() {
        given()
        .get("/users/admin-commands/get-users")
        .then()
        .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = { "admin" })
    void shouldAccessAdminWhenAdminAuthenticated() {

        given()
        .get("/users/admin-commands/get-users")
        .then()
        .statusCode(Status.OK.getStatusCode());

    }

    @Test
    @Order(5)
    @TestSecurity(user = "user1", roles = { "user" })
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        Response res = given()
        .when()
        .get("/users/me");
        assertEquals(Status.OK.getStatusCode(), res.statusCode());
    }
}

