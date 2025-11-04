package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.test.utils.JsonUtils;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
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

    @MockitoBean
    ClienteService service;

    @Autowired
    private ObjectMapper objectMapper;

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

    /*@Test
    public void testCreateBook() throws Exception {
        // Mock data
        Cliente cliente = Cliente.builder()
                                 .nome("Kaunda Daniel Masutic Da Gama")
                                 .telefone("+244924192969")
                                 .numeroBi("001234567BA045")
                                 .email("kaunda.daniel@email.com")
                                 .status(ClienteStatus.ATIVO)
                                 .build();

        // Send POST request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente/save")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(JsonUtils.toJson(cliente)))
                                  .andExpect(MockMvcResultMatchers.status().isCreated())
                                  .andReturn();

        // Deserialize response
        Cliente createCliente   = objectMapper.readValue(result.getResponse().getContentAsString(), Cliente.class);

        // Assertions
        assertNotNull(createCliente.getId());
        assertEquals(createCliente.getNome(), createCliente.getNome());
        assertEquals(createCliente.getStatus(), createCliente.getStatus());
    }

    @Test
    void deveCriarEListarClienteComSucesso() throws Exception {

        Cliente cliente = Cliente.builder()
                                 .nome("Kaunda Daniel Masutic Da Gama")
                                 .telefone("+244924192969")
                                 .numeroBi("001234567BA045")
                                 .email("kaunda.daniel@email.com")
                                 .status(ClienteStatus.ATIVO)
                                 .build();

        Cliente cliente2 = Cliente.builder()
                                  .nome("Armando Pupilo do Kaunda Daniel")
                                  .telefone("+244924192980")
                                  .numeroBi("001234765AR045")
                                  .email("armando.pupilo@email.com")
                                  .status(ClienteStatus.ATIVO)
                                  .build();

        ClienteRequest request = new ClienteRequest(cliente.getNome(), "10/02/1990", cliente.getTelefone(), cliente.getNumeroBi(), cliente.getEmail());

        ClienteResponse response1 = new ClienteResponse(cliente.getNome(), "10/02/1990", cliente.getTelefone(), cliente.getNumeroBi(), cliente.getEmail());
        ClienteResponse response2 = new ClienteResponse(cliente2.getNome(), "01/07/1995", cliente2.getTelefone(), cliente2.getNumeroBi(), cliente2.getEmail());

        // Envia o POST

        when(service.save(request)).thenReturn(response1);

        mockMvc.perform(post("/api/cliente/save")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(response1)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome").value(response1.getNome()))
                    .andExpect(jsonPath("$.telefone").value(response1.getTelefone()))
                    .andExpect(jsonPath("$.numeroBi").value(response1.getNumeroBi()))
                    .andExpect(jsonPath("$.email").value(response1.getEmail()))
                    .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void listAll() throws Exception {

        Cliente cliente = Cliente.builder()
                                 .nome("Kaunda Daniel Masutic Da Gama")
                                 .telefone("+244924192969")
                                 .numeroBi("001234567BA045")
                                 .email("kaunda.daniel@email.com")
                                 .status(ClienteStatus.ATIVO)
                                 .build();

        Cliente cliente2 = Cliente.builder()
                                  .nome("Armando Pupilo do Kaunda Daniel")
                                  .telefone("+244924192980")
                                  .numeroBi("001234765AR045")
                                  .email("armando.pupilo@email.com")
                                  .status(ClienteStatus.ATIVO)
                                  .build();

        ClienteResponse response1 = new ClienteResponse(cliente.getNome(), "10/02/1990", cliente.getTelefone(), cliente.getNumeroBi(), cliente.getEmail());
        ClienteResponse response2 = new ClienteResponse(cliente2.getNome(), "01/07/1995", cliente2.getTelefone(), cliente2.getNumeroBi(), cliente2.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/list")
                        .param("search", "")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(response1.getNome()))
                .andExpect(jsonPath("$[0].nascimento").value(response1.getNascimento()))
                .andExpect(jsonPath("$[0].numeroBi").value(response1.getNumeroBi()))
                .andExpect(jsonPath("$[0].email").value(response1.getEmail()))

                .andExpect(jsonPath("$[1].nome").value(response2.getNome()))
                .andExpect(jsonPath("$[1].nascimento").value(response2.getNascimento()))
                .andExpect(jsonPath("$[1].numeroBi").value(response2.getNumeroBi()))
                .andExpect(jsonPath("$[1].email").value(response2.getEmail()))

                .andDo(MockMvcResultHandlers.print());
    }*/
}
