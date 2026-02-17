package ao.tcc.projetofinal.jecuz.entities;

import ao.tcc.projetofinal.jecuz.enums.TipoLimpeza;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdensDeServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numOrdemServico;

    private String nomeCliente;

    private String dataSolicitacao; //padrão 01/01/2000

    @Enumerated(EnumType.STRING)
    private TipoLimpeza tipoLimpeza;

    private Double valorTotal;

    private String dataExecucao;

    @ManyToOne
    @JoinColumn(name = "diarista_id")
    @JsonBackReference
    private Diarista diarista;
}
