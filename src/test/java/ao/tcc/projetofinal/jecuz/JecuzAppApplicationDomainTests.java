package ao.tcc.projetofinal.jecuz;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class JecuzAppApplicationDomainTests {

    @Autowired
    ClienteRepository repository;

    @LocalServerPort
    Integer port;

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        System.out.println("url: " + container.getJdbcUrl());
        System.out.println("username: " + container.getUsername());
        System.out.println("password: " + container.getPassword());
        System.out.println("spring.datasource.driver-class-name: " + container.getJdbcDriverInstance());
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        repository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    @Test
    void testDomain(){

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

        String nome = "Kaunda Daniel";
        LocalDate nascimento = LocalDate.of(1990,02,10);
        String telefone = "+244951753147";
        String numeroBi = "009547305KD104";
        String email = "kaunda.daniel@mail.com";
        LocalDateTime dataRegistro = LocalDateTime.now();
        ClienteStatus status = ClienteStatus.ATIVO;

        String nome2 = "Armando Kalei";
        LocalDate nascimento2 = LocalDate.of(1990,11,14);
        String telefone2 = "+244951753178";
        String numeroBi2 = "009547305AK106";
        String email2 = "armando.kalei@mail.com";
        LocalDateTime dataRegistro2 = LocalDateTime.now();
        ClienteStatus status2 = ClienteStatus.ATIVO;

        Cliente c1 = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);
        Cliente c2 = new Cliente(null, nome2, nascimento2, telefone2, numeroBi2, email2, dataRegistro2, status2, null);

        List<Cliente> clients = Arrays.asList(c1, c2);

        List<Cliente> clientsAll = repository.saveAll(clients);

        List<Cliente> found = clientsAll.stream().toList();
        assertEquals(2, found.size());
    }
}
