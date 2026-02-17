package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.OrdensDeServicoController;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoResponse;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.enums.TipoLimpeza;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.exceptions.ValidationParameterException;
import ao.tcc.projetofinal.jecuz.services.ordens.OrdensDeServicoService;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrdensDeServicoController.class, excludeAutoConfiguration = {JacksonAutoConfiguration.class})
@Import(OrdensDeServicoControllerTest.TestConfig.class)
public class OrdensDeServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrdensDeServicoService ordensDeServicoService;

    @Test
    void gerarShouldReturnCreated() throws Exception {
        OrdemServicoRequest request = new OrdemServicoRequest();
        request.setDataSolicitacao("10/01/2025");
        request.setDataExecucao("11/01/2025");
        request.setDescricaoTarefa("Limpeza completa");
        request.setValorTotal(150.0);

        OrdensDeServico os = new OrdensDeServico();
        os.setId(1L);
        os.setNomeCliente("Cliente Teste");

        Mockito.when(ordensDeServicoService.gerarOrdem(anyString(), anyString(), any(OrdemServicoRequest.class), any(TipoLimpeza.class)))
                .thenReturn(os);

        mockMvc.perform(post("/api/ordem/servico/gerar")
                        .param("idCliente", "1")
                        .param("idDiarista", "1")
                        .param("tipoLimpeza", TipoLimpeza.RESIDENCIAL.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"));
    }

    @Test
    void gerarShouldReturnBadRequest_whenServiceThrowsValidationParameterException() throws Exception {
        OrdemServicoRequest request = new OrdemServicoRequest();
        request.setDataSolicitacao("10/01/2025");
        request.setDataExecucao("11/01/2025");

        Mockito.when(ordensDeServicoService.gerarOrdem(anyString(), anyString(), any(OrdemServicoRequest.class), any(TipoLimpeza.class)))
                .thenThrow(new ValidationParameterException("Parâmetro Inválido"));

        mockMvc.perform(post("/api/ordem/servico/gerar")
                        .param("idCliente", "abc")
                        .param("idDiarista", "1")
                        .param("tipoLimpeza", TipoLimpeza.RESIDENCIAL.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listShouldReturnOk() throws Exception {
        OrdemServicoResponse resp = new OrdemServicoResponse();
        DiaristaResponse diarista = new DiaristaResponse();
        diarista.setNome("Cliente A");
        resp.setDiarista(diarista);

        PageableCommons<List<OrdemServicoResponse>> page = new PageableCommons<>(List.of(resp), 0, 1, 1, 1);

        Mockito.when(ordensDeServicoService.listOS(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/ordem/servico/list")
                        .param("search", "")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].diarista.nome").value("Cliente A"));
    }

    @Test
    void listShouldReturnBadRequest_whenPageParamInvalid() throws Exception {
        mockMvc.perform(get("/api/ordem/servico/list")
                        .param("search", "")
                        .param("page", "notNumber")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdShouldReturnOk() throws Exception {
        OrdensDeServico os = new OrdensDeServico();
        os.setId(1L);
        os.setNomeCliente("Cliente Encontrado");

        Mockito.when(ordensDeServicoService.findOS("1")).thenReturn(os);

        mockMvc.perform(get("/api/ordem/servico")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Encontrado"));
    }

    @Test
    void findByIdShouldReturnNotFound_whenServiceThrowsRegraDeNegocioException() throws Exception {
        Mockito.when(ordensDeServicoService.findOS("999"))
                .thenThrow(new RegraDeNegocioException("O.S não encontrado"));

        mockMvc.perform(get("/api/ordem/servico").param("id", "999"))
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OrdensDeServicoService ordensDeServicoService() {
            return Mockito.mock(OrdensDeServicoService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
