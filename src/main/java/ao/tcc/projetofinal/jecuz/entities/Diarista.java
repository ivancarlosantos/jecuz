package ao.tcc.projetofinal.jecuz.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Diarista implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Date nascimento;

    private String telefone;

    private String numeroBi;

    private String email;

    private String verificationCode;

    private boolean enabled;

    @OneToOne(mappedBy = "diarista")
    private Cliente cliente;
}
