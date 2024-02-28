package ao.tcc.projetofinal.jecuz.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaristaDTO {

    private Long id;

    @NotEmpty(message = "campo obrigatório")
    @NotBlank
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
    @Email
    private String email;

    private String verificationCode;

    private boolean enabled;

    @JsonIgnore
    private ClienteDTO cliente;

    private List<OrdensDeServicoDTO> ordensDeServicos;
}
