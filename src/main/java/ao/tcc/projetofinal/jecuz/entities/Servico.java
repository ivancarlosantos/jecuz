package ao.tcc.projetofinal.jecuz.entities;

import ao.tcc.projetofinal.jecuz.enums.Icone;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;

    private BigDecimal valorMinimo;

    private Integer qtdHoras;

    private BigDecimal percentagemComissao;

    private Integer horasQuarto;

    private BigDecimal valorQuarto;

    private Integer horasSala;

    private BigDecimal valorSala;

    private Integer horasBanheiro;


    private BigDecimal valorBanheiro;

    private Integer horasCozinha;

    private BigDecimal valorCozinha;

    private Integer horasQuintal;


    private BigDecimal valorQuintal;

    private Integer horasOutros;

    private BigDecimal valorOutros;

    @Enumerated(EnumType.STRING)
    private Icone icone;
    @Column(nullable = false)
    private Integer posicao;
}
