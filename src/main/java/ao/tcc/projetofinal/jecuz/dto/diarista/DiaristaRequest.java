package ao.tcc.projetofinal.jecuz.dto.diarista;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdensDeServicoDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
