package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteContainerTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));

    @LocalServerPort
    Integer port;

    @Autowired
    ClienteRepository repository;

    private Cliente clienteBase;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        System.out.println("url: " + container.getJdbcUrl());
        System.out.println("username: " + container.getUsername());
        System.out.println("password: " + container.getPassword());
        System.out.println("spring.datasource.driver-class-name: " + container.getJdbcDriverInstance());
    }

    @BeforeEach
    void setUp() throws ParseException {
        repository.deleteAll();

        clienteBase = Cliente.builder()
                             .nome("Kaunda Daniel Masutic Da Gama")
                             .nascimento(LocalDate.now())
                             .telefone("+244924192969")
                             .numeroBi("001234567BA045")
                             .email("kaunda.daniel@email.com")
                             .status(ClienteStatus.ATIVO)
                             .build();

        RestAssured.baseURI = "http://localhost:" + port;

        repository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    @Test
    void deveSalvageRecuperateClient() {
        Cliente cliente = new Cliente();
        cliente.setNome("Kaunda Daniel Masutic Da Gama");
        cliente.setNascimento(LocalDate.now());
        cliente.setTelefone("+244 924 192 969");
        cliente.setNumeroBi("758483258KD987");
        cliente.setEmail("kaunda@mail.com");
        cliente.setStatus(ClienteStatus.ATIVO);

        Cliente salvo = repository.save(cliente);
        Optional<Cliente> encontrado = repository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Kaunda Daniel Masutic Da Gama", encontrado.get().getNome());
        assertEquals(ClienteStatus.ATIVO, encontrado.get().getStatus());
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodosOsClientes() {
        // Given
        repository.save(clienteBase);

        Cliente cliente2 = Cliente.builder()
                                  .nome("Armando Pupilo do Kaunda Daniel")
                                  .nascimento(LocalDate.now())
                                  .telefone("244934567890")
                                  .numeroBi("003456789BA047")
                                  .email("kaunda.daniel@email.com")
                                  .status(ClienteStatus.ATIVO)
                                  .build();

        repository.save(cliente2);

        // When
        List<Cliente> clientes = repository.findAll();

        // Then
        assertThat(clientes).hasSize(2);
        assertThat(clientes).extracting(Cliente::getNome)
                            .containsExactlyInAnyOrder("Kaunda Daniel Masutic Da Gama", "Armando Pupilo do Kaunda Daniel");
    }

    @Test
    @DisplayName("Deve deletar cliente")
    void deveDeletarCliente() {
        // Given
        Cliente clienteSalvo = repository.save(clienteBase);

        // When
        repository.deleteById(clienteSalvo.getId());

        // Then
        Optional<Cliente> clienteDeletado = repository.findById(clienteSalvo.getId());
        assertThat(clienteDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar cliente existente")
    void deveAtualizarClienteExistente() {
        // Given
        Cliente clienteSalvo = repository.save(clienteBase);

        // When
        clienteSalvo.setNome("Armando Pupilo do Kaunda Daniel");
        clienteSalvo.setStatus(ClienteStatus.INATIVO);
        Cliente clienteAtualizado = repository.save(clienteSalvo);

        // Then
        assertThat(clienteAtualizado.getId()).isEqualTo(clienteSalvo.getId());
        assertThat(clienteAtualizado.getNome()).isEqualTo("Armando Pupilo do Kaunda Daniel");
        assertThat(clienteAtualizado.getStatus()).isEqualTo(ClienteStatus.INATIVO);
    }
}
