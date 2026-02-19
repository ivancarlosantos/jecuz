package ao.tcc.projetofinal.jecuz.test.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

/**
 * End-to-End Tests for Clientes API
 * Complete flow tests using RestAssured and TestContainers
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteE2ETest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testCreateClienteFlow() {
        given()
                .queryParam("nome", "João Teste E2E")
                .queryParam("nascimento", "10/01/1990")
                .queryParam("telefone", "+244921234567")
                .queryParam("numeroBi", "000000001AA001")
                .queryParam("email", "joao.e2e@test.com")
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201);
    }

    @Test
    void testListAllClientesFlow() {
        given()
                .when()
                .get("/api/cliente/list")
                .then()
                .statusCode(200);
    }

    @Test
    void testFullClienteLifecycle() {
        // Create
        var response = given()
                .queryParam("nome", "Maria Lifecycle")
                .queryParam("nascimento", "15/05/1992")
                .queryParam("telefone", "+244929876543")
                .queryParam("numeroBi", "000000002BB002")
                .queryParam("email", "maria.lifecycle@test.com")
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Debug: Print response body to understand structure
        System.out.println("Response body: " + response.getBody().asString());

        // List
        given()
                .when()
                .get("/api/cliente/list")
                .then()
                .statusCode(200);
    }
}

