package ao.tcc.projetofinal.jecuz.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Diarista  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String nascimento;

    private String telefone;

    private String numeroBi;

    private String email;

    private String verificationCode;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "diarista", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrdensDeServico> ordensDeServicos;
}
