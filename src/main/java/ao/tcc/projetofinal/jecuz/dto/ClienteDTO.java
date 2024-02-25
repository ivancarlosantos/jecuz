package ao.tcc.projetofinal.jecuz.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String nome;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String nascimento;  // padrão 01/01/2000

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String telefone;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String numeroBi;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    @Email
    private String email;

    private DiaristaDTO diarista;
}
