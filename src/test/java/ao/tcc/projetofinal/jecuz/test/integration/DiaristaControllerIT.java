package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.DiaristaController;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.services.diarista.DiaristaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DiaristaController.class, excludeAutoConfiguration = {JacksonAutoConfiguration.class})
@Import(DiaristaControllerIT.TestConfig.class)
public class DiaristaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiaristaService diaristaService;

    @Test
    void saveShouldReturnCreated() throws Exception {
        DiaristaRequest request = new DiaristaRequest("Maria", "01/01/1990", "+244922222222", "001234567BA045", "maria@email.com");
        DiaristaResponse response = new DiaristaResponse("Maria", "+244922222222", "maria@email.com");

        when(diaristaService.save(any(DiaristaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/diarista/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(response.getNome())))
                .andExpect(jsonPath("$.telefone", is(response.getTelefone())))
                .andExpect(jsonPath("$.email", is(response.getEmail())));
    }

    @Test
    void saveShouldReturnBadRequestWhenEmailMissing() throws Exception {
        DiaristaRequest request = new DiaristaRequest("Joao", "01/01/1990", "+244911111111", "001234567BA046", "");

        mockMvc.perform(post("/api/diarista/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdShouldReturnOk() throws Exception {
        String id = "abc-123";
        DiaristaSOResponse response = new DiaristaSOResponse();
        response.setNome("Ana");

        when(diaristaService.findByID(id)).thenReturn(response);

        mockMvc.perform(get("/api/diarista").param("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(response.getNome())));
    }

    @Test
    void findByIdShouldReturnServerErrorWhenNotFound() throws Exception {
        String id = "not-found";

        when(diaristaService.findByID(id)).thenThrow(new RuntimeException("não encontrado"));

        Exception ex = assertThrows(Exception.class, () -> mockMvc.perform(get("/api/diarista").param("id", id)).andReturn());
        Throwable root = ex;
        while (root.getCause() != null) root = root.getCause();
        assertTrue(root instanceof RuntimeException);
        assertEquals("não encontrado", root.getMessage());
    }

    @Test
    void updateShouldReturnAccepted() throws Exception {
        Long id = 1L;
        DiaristaRequest request = new DiaristaRequest("Carlos", "01/01/1990", "+244933333333", "001234567BA047", "carlos@email.com");

        // controller currently returns accepted with empty body; no service call required
        mockMvc.perform(put("/api/diarista").param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DiaristaService diaristaService() {
            return Mockito.mock(DiaristaService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
