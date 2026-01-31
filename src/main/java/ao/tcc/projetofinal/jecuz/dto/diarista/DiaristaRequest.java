package ao.tcc.projetofinal.jecuz.dto.diarista;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaristaRequest {

    private String nome;

    private String nascimento;  // padrão 01/01/1999

    private String telefone;

    private String numeroBi;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    @Email
    private String email;

}
