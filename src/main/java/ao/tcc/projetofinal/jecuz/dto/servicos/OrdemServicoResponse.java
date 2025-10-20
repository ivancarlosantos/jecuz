package ao.tcc.projetofinal.jecuz.dto.servicos;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdemServicoResponse {

    private String dataSolicitacao; //padrão 01/01/2000

    private String dataExecucao;

    private Double valorTotal;

    private DiaristaResponse diarista;
}
