package ao.tcc.projetofinal.jecuz.entities;

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
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Date nascimento;

    private String telefone;

    private String numeroBi;

    private String email;

    @JsonManagedReference
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Diarista> diaristas;
}
