package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.ClienteController;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration Tests for ClienteController
 * Tests HTTP layer with mocked service layer
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateClienteAndReturnCreated() throws Exception {
        ClienteResponse response = ClienteResponse.builder()
                .id(1L)
                .nome("João Silva")
                .nascimento("15/03/1985")
                .telefone("+244921234567")
                .numeroBi("123456789")
                .email("joao@example.com")
                .build();

        when(clienteService.save(any(ClienteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cliente/save")
                .param("nome", "João Silva")
                .param("nascimento", "15/03/1985")
                .param("telefone", "+244921234567")
                .param("numeroBi", "123456789")
                .param("email", "joao@example.com"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenClienteIsInvalid() throws Exception {
        when(clienteService.save(any(ClienteRequest.class))).thenThrow(new IllegalArgumentException("Invalid cliente"));

        mockMvc.perform(post("/api/cliente/save")
                .param("nome", "")
                .param("nascimento", "40/13/2000")
                .param("telefone", "")
                .param("numeroBi", "")
                .param("email", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetClienteById() throws Exception {
        ClienteResponse response = ClienteResponse.builder()
                .id(1L)
                .nome("Maria Santos")
                .nascimento("20/05/1990")
                .telefone("+244929876543")
                .numeroBi("987654321")
                .email("maria@example.com")
                .build();

        when(clienteService.findByID("1")).thenReturn(response);

        mockMvc.perform(get("/api/cliente/findByID")
                .param("findByID", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Santos"));
    }

    @Test
    void shouldReturnNotFoundWhenClienteDoesNotExist() throws Exception {
        when(clienteService.findByID("999")).thenThrow(new RegraDeNegocioException("Cliente not found"));

        mockMvc.perform(get("/api/cliente/findByID")
                .param("findByID", "999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

