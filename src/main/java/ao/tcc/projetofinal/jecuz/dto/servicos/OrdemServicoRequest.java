package ao.tcc.projetofinal.jecuz.dto.servicos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdemServicoRequest {

    private String nomeCliente;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String dataSolicitacao; //padrão 01/01/2000

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String descricaoTarefa;

    private Double valorTotal;

    private String dataExecucao;

}
