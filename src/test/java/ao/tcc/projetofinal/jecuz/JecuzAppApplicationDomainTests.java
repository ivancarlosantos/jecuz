package ao.tcc.projetofinal.jecuz;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JecuzAppApplicationDomainTests {

    @LocalServerPort
    Integer port;

    @Autowired
    ClienteRepository repository;

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        log.info("url {}", container.getJdbcUrl());
        log.info("username {}", container.getUsername());
        log.info("password {}", container.getPassword());
        log.info("spring.datasource.driver-class-name {}", container.getJdbcDriverInstance());
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        repository.deleteAll();
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void testDomain(){

        Long id = 1L;
        String nome = "Kaunda Daniel";
        LocalDate nascimento = LocalDate.of(1990,02,10);
        String telefone = "+244951753147";
        String numeroBi = "009547305KD104";
        String email = "kaunda.daniel@mail.com";
        LocalDateTime dataRegistro = LocalDateTime.now();
        ClienteStatus status = ClienteStatus.ATIVO;

        Cliente cliente = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);

        repository.save(cliente);

        assertNotNull(cliente);
        assertEquals(nome, cliente.getNome());
        assertEquals(nascimento, cliente.getNascimento());
        assertEquals(telefone, cliente.getTelefone());
        assertEquals(numeroBi, cliente.getNumeroBi());
        assertEquals(email, cliente.getEmail());
        assertEquals(dataRegistro, cliente.getDataRegistro());
        assertEquals(status, cliente.getStatus());
    }

    @Test
    void testDomainListAll() {

        Long id = 1L;
        String nome = "Kaunda Daniel";
        LocalDate nascimento = LocalDate.of(1990,02,10);
        String telefone = "+244951753147";
        String numeroBi = "009547305KD104";
        String email = "kaunda.daniel@mail.com";
        LocalDateTime dataRegistro = LocalDateTime.now();
        ClienteStatus status = ClienteStatus.ATIVO;

        Cliente c1 = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);
        Cliente c2 = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);

        List<Cliente> clients = Arrays.asList(c1, c2);

        repository.saveAll(clients);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/cliente/list")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
