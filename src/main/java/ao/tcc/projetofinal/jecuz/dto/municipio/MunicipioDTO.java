package ao.tcc.projetofinal.jecuz.dto.municipio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MunicipioDTO implements Serializable {

    private Long id;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String cep;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String logradouro;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String complemento;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String bairro;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String localidade;

    @NotNull
    @NotBlank(message = "campo obrigatório")
    private String uf;
}
