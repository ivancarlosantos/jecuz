package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.ClienteController;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClienteController.class)
@Import(ClienteControllerIT.TestConfig.class)
public class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteService clienteService;

    @Test
    void saveShouldReturnCreated() throws Exception {
        ClienteRequest request = new ClienteRequest("Fulano", "01/01/1990", "+244911111111", "001234567BA100", "fulano@email.com");
        ClienteResponse response = new ClienteResponse(request.getNome(), request.getNascimento(), request.getTelefone(), request.getNumeroBi(), request.getEmail());

        when(clienteService.save(any(ClienteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cliente/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(response.getNome())));
    }

    @Test
    void listShouldReturnOk() throws Exception {
        // retorno coberto no ClienteIntegrationTest existente; aqui apenas smoke test de mapeamento
        mockMvc.perform(get("/api/cliente/list").param("search", "").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnOk() throws Exception {
        String id = "1";
        ClienteResponse response = new ClienteResponse("Nome", "01/01/1990", "+244911111111", "001234567BA100", "a@b.com");

        when(clienteService.findByID(id)).thenReturn(response);

        mockMvc.perform(get("/api/cliente/findByID").param("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(response.getNome())));
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
