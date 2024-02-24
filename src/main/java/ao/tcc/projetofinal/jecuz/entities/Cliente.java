package ao.tcc.projetofinal.jecuz.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nome_completo", nullable = false)
    private String nomeCompleto;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true, length = 14)
    private String numeroBi;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval=true)
    @JoinColumn(name = "foto_documento", nullable = true)
    private Foto fotoDocumento;

    @ManyToMany
    @JoinTable(
            name = "municipio_cliente",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "municipio_id"))
    private List<Municipio> municipal;

}
