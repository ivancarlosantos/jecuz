package ao.tcc.projetofinal.jecuz.dto.servicos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdensDeServicoDTO {

    private String numOrdemServico;

    private String cliente;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String dataSolicitacao; //padrão 01/01/2000

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String descricaoTarefa;

    private Double valorTotal;

    private LocalDateTime dataExecucao;

    //private DiaristaDTO diarista;
}
