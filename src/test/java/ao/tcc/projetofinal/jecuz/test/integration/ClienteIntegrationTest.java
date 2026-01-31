package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.ClienteController;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClienteController.class, excludeAutoConfiguration = {JacksonAutoConfiguration.class})
@Import(ClienteIntegrationTest.TestConfig.class)
public class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteService clienteService;

    @Test
    public void testCreateBook() throws Exception {
        ClienteRequest request = new ClienteRequest("Kaunda Daniel Masutic Da Gama", "10/02/1990", "+244924192969", "001234567BA045", "kaunda.daniel@email.com");
        ClienteResponse response = new ClienteResponse(request.getNome(), request.getNascimento(), request.getTelefone(), request.getNumeroBi(), request.getEmail());

        when(clienteService.save(any(ClienteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cliente/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(response.getNome())))
                .andExpect(jsonPath("$.telefone", is(response.getTelefone())));
    }

    @Test
    void deveCriarEListarClienteComSucesso() throws Exception {
        ClienteRequest request = new ClienteRequest("Kaunda Daniel Masutic Da Gama", "10/02/1990", "+244924192969", "001234567BA045", "kaunda.daniel@email.com");
        ClienteResponse response = new ClienteResponse(request.getNome(), request.getNascimento(), request.getTelefone(), request.getNumeroBi(), request.getEmail());

        when(clienteService.save(any(ClienteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cliente/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(response.getNome())))
                .andExpect(jsonPath("$.telefone", is(response.getTelefone())))
                .andExpect(jsonPath("$.numeroBi", is(response.getNumeroBi())))
                .andExpect(jsonPath("$.email", is(response.getEmail())));
    }

    @Test
    void listAll() throws Exception {
        ClienteResponse response1 = new ClienteResponse("Kaunda Daniel Masutic Da Gama", "10/02/1990", "+244924192969", "001234567BA045", "kaunda.daniel@email.com");
        ClienteResponse response2 = new ClienteResponse("Armando Pupilo do Kaunda Daniel", "01/07/1995", "+244924192980", "001234765AR045", "armando.pupilo@email.com");

        PageableCommons<List<ClienteResponse>> pageable = new PageableCommons<>(List.of(response1, response2), 0, 2, 1, 2);
        when(clienteService.listAll(anyString(), anyInt(), anyInt())).thenReturn(pageable);

        mockMvc.perform(get("/api/cliente/list")
                        .param("search", "")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome", is(response1.getNome())))
                .andExpect(jsonPath("$.content[0].nascimento", is(response1.getNascimento())))
                .andExpect(jsonPath("$.content[1].nome", is(response2.getNome())))
                .andExpect(jsonPath("$.content[1].nascimento", is(response2.getNascimento())));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
