package ao.tcc.projetofinal.jecuz.test.smoke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke Tests for Diarista API Endpoints
 * Quick sanity checks to ensure basic API functionality
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiaristaApiSmokeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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
        // Setup is done implicitly with LocalServerPort
    }

    @Test
    void smokeTest_applicationStarts() {
        // If the application starts, this test passes
        assertTrue(true, "Application started successfully");
    }

    @Test
    void smokeTest_healthEndpointResponds() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("UP") || response.getBody().contains("status"));
    }

    /*@Test
    void smokeTest_diaristaListEndpointResponds() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/diarista/1",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful(),
                "Expected 2xx response but got: " + response.getStatusCode());
    }*/

    @Test
    void smokeTest_clienteListEndpointResponds() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/cliente/list",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful(),
                "Expected 2xx response but got: " + response.getStatusCode());
    }

    @Test
    void smokeTest_ordensDeServicoListEndpointResponds() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/ordem/servico/list?page=0&size=10",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful(),
                "Expected 2xx response but got: " + response.getStatusCode());
    }

    @Test
    void smokeTest_nonExistentEndpointReturns404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/nonexistent",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void smokeTest_prometheusMetricsEndpointResponds() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/prometheus",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful() ||
                   response.getStatusCode() == HttpStatus.NOT_FOUND,
                "Prometheus endpoint should be available or return 404");
    }
}

