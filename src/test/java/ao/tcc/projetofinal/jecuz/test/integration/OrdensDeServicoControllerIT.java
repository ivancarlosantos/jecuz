package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.OrdensDeServicoController;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.enums.TipoLimpeza;
import ao.tcc.projetofinal.jecuz.services.ordens.OrdensDeServicoService;
import ao.tcc.projetofinal.jecuz.utils.GerarNumeroOS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrdensDeServicoController.class)
public class OrdensDeServicoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdensDeServicoService ordensDeServicoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void gerarShouldReturnCreated() throws Exception {
        OrdemServicoRequest req = new OrdemServicoRequest();
        req.setDataSolicitacao("01/01/2024");
        req.setDataExecucao("02/01/2024");
        req.setValorTotal(100.0);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome(String.valueOf(Faker.instance().name()));
        Diarista diarista = new Diarista();
        diarista.setId(2L);
        diarista.setNome(String.valueOf(Faker.instance().name()));
        OrdensDeServico os = new OrdensDeServico();
        os.setId(1L);
        os.setNumOrdemServico(GerarNumeroOS.gerar());
        os.setNomeCliente(Faker.instance().name().fullName());
        os.setDataSolicitacao("01/01/2024");
        os.setDataExecucao("02/01/2024");
        os.setTipoLimpeza(TipoLimpeza.RESIDENCIAL);
        os.setValorTotal(100.0);
        os.setDiarista(diarista);

        when(ordensDeServicoService.gerarOrdem(eq(cliente.getId().toString()), eq(diarista.getId().toString()), any(OrdemServicoRequest.class), eq(TipoLimpeza.RESIDENCIAL)))
                .thenReturn(os);

        mockMvc.perform(post("/api/ordem/servico/gerar?idCliente=1&idDiarista=2&tipoLimpeza=RESIDENCIAL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
