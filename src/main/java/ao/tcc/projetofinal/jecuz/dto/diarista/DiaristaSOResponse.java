package ao.tcc.projetofinal.jecuz.dto.diarista;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdensDeServicoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaristaSOResponse {

    private String nome;

    private List<OrdensDeServicoDTO> ordensServicos;
}
