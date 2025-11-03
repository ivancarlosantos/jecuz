package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteIntegrationTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ClienteRepository repository;

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
    void setup() {
        repository.deleteAll();
    }

    @Test
    void deveCriarEListarClienteComSucesso() throws Exception {

        Cliente cliente = Cliente.builder()
                                 .nome("Kaunda Daniel Masutic Da Gama")
                                 .nascimento(LocalDate.now())
                                 .telefone("+244924192969")
                                 .numeroBi("001234567BA045")
                                 .email("kaunda.daniel@email.com")
                                 .dataRegistro(LocalDateTime.now())
                                 .status(ClienteStatus.ATIVO)
                                 .build();

        Cliente cliente2 = Cliente.builder()
                                  .nome("Armando Pupilo do Kaunda Daniel")
                                  .nascimento(LocalDate.now().plusMonths(3))
                                  .telefone("+244924192980")
                                  .numeroBi("001234765AR045")
                                  .email("armando.pupilo@email.com")
                                  .dataRegistro(LocalDateTime.now())
                                  .status(ClienteStatus.ATIVO)
                                  .build();

        // Envia o POST
        mockMvc.perform(post("/api/cliente/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(cliente.getNome()))
                .andDo(MockMvcResultHandlers.print());

        // Faz o GET e valida se o cliente foi salvo
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(cliente.getNome()))
                .andExpect(jsonPath("$[0].nascimento").value(cliente.getNascimento()))
                .andExpect(jsonPath("$[0].numeroBi").value(cliente.getNumeroBi()))
                .andExpect(jsonPath("$[0].email").value(cliente.getEmail()))

                .andExpect(jsonPath("$[1].nome").value(cliente2.getNome()))
                .andExpect(jsonPath("$[1].nascimento").value(cliente.getNascimento()))
                .andExpect(jsonPath("$[1].numeroBi").value(cliente2.getNumeroBi()))
                .andExpect(jsonPath("$[1].email").value(cliente2.getEmail()))

                .andDo(MockMvcResultHandlers.print());
    }
}
