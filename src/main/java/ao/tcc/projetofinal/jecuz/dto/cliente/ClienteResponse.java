package ao.tcc.projetofinal.jecuz.dto.cliente;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponse {

    private Long id;

    private String nome;

    private String nascimento;

    private String telefone;

    private String numeroBi;

    private String email;
}
