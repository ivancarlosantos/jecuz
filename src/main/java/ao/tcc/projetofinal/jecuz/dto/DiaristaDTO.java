package ao.tcc.projetofinal.jecuz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaristaDTO {

    private Long id;

    @NotEmpty(message = "campo obrigatório")
    @NotNull
    private String nome;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String nascimento;  // padrão 01/01/1999

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String telefone;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String numeroBi;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
    private String email;
}
