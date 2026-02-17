package ao.tcc.projetofinal.jecuz.entities;

import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate nascimento;

    private String telefone;

    private String numeroBi;

    private String email;

    private LocalDateTime dataRegistro;

    @Enumerated(value = EnumType.STRING)
    private ClienteStatus status;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference
    private List<Diarista> diaristas;
}
