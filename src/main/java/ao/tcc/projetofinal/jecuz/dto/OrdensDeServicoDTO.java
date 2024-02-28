package ao.tcc.projetofinal.jecuz.dto;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class
 OrdensDeServicoDTO {

    private Long id;

    private String numOrdemServico;

    private String cliente;

    private String nomeDiarista;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String dataSolicitacao; //padrão 01/01/2000

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String descricaoTarefa;

    private Double valorTotal;

    private LocalDateTime dataExecucao;

    private DiaristaDTO diarista;
}
