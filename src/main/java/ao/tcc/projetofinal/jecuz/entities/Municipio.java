package ao.tcc.projetofinal.jecuz.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    @Column(name = "code_municipio", nullable = false)
    private String codeMunicipio;

    @Column(name = "nome_municipio")
    private String nome;

    @ManyToMany(mappedBy = "municipios")
    private List<Diarista> diaristas;

    @ManyToMany(mappedBy = "municipal")
    private List<Cliente> clientes;
}
