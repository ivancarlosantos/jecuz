package ao.tcc.projetofinal.jecuz.dto.diarista;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaristaResponse {

    private String nome;

    private String telefone;

    private String email;
}
