package ao.tcc.projetofinal.jecuz.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private Long contentLength;
    private String contentType;
    private String url;
    @OneToOne
    @MapsId
    private Diarista diarista;

    @OneToOne
    @MapsId
    private Cliente cliente;
}
