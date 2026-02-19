package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.DiaristaController;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdensDeServicoDTO;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.enums.TipoLimpeza;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.services.diarista.DiaristaService;
import ao.tcc.projetofinal.jecuz.utils.GerarNumeroOS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration Tests for DiaristaController
 * Tests HTTP layer with mocked service layer
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DiaristaController.class)
public class DiaristaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiaristaService diaristaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateDiaristaAndReturnCreated() throws Exception {
        DiaristaResponse response = new DiaristaResponse(
                "Ana Silva",
                "+244927584753",
                "ana@example.com"
        );

        when(diaristaService.save(any(DiaristaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/diarista/save")
                .param("nome", "Ana Silva")
                .param("nascimento", "20/06/1988")
                .param("telefone", "+244927584753")
                .param("numeroBi", "006242029UE045")
                .param("email", "ana@example.com"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ana Silva"))
                .andExpect(jsonPath("$.email").value("ana@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenDiaristaIsInvalid() throws Exception {
        when(diaristaService.save(any(DiaristaRequest.class))).thenThrow(new IllegalArgumentException("Invalid diarista"));

        mockMvc.perform(post("/api/diarista/save")
                .param("nome", "")
                .param("nascimento", "invalid-date")
                .param("telefone", "")
                .param("numeroBi", "")
                .param("email", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetDiaristaById() throws Exception {
        String diarista = "Paula Costa";
        OrdensDeServico svc = OrdensDeServico.builder().numOrdemServico(GerarNumeroOS.gerar()).nomeCliente("Carla Santos").dataSolicitacao("17/02/2026").tipoLimpeza(TipoLimpeza.RESIDENCIAL).valorTotal(150.0).diarista(Diarista.builder().nome(diarista).build()).build();
        OrdensDeServicoDTO dto = OrdensDeServicoDTO.builder().numOrdemServico(svc.getNumOrdemServico()).cliente(svc.getNomeCliente()).dataSolicitacao(svc.getDataSolicitacao()).descricaoTarefa(TipoLimpeza.RESIDENCIAL.toString()).valorTotal(svc.getValorTotal()).dataExecucao(LocalDateTime.now()).build();
        List<OrdensDeServicoDTO> os = List.of(dto);

        DiaristaSOResponse response = new DiaristaSOResponse(svc.getDiarista().getNome(), os);

        when(diaristaService.findByID("1")).thenReturn(response);

        mockMvc.perform(get("/api/diarista")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(diarista));
    }

    @Test
    void shouldReturnNotFoundWhenDiaristaDoesNotExist() throws Exception {
        when(diaristaService.findByID("999")).thenThrow(new RegraDeNegocioException("Diarista not found"));

        mockMvc.perform(get("/api/diarista")
                .param("id", "999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

