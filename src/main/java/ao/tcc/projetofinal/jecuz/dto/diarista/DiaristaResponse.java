package ao.tcc.projetofinal.jecuz.dto.diarista;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaristaResponse {

    private String nome;

    private String telefone;

    private String email;
}
