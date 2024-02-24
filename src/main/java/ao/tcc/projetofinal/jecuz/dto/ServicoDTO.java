package ao.tcc.projetofinal.jecuz.dto;

import ao.tcc.projetofinal.jecuz.enums.Icone;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicoDTO {


    private Long id;
    @NotEmpty(message = "Não pode estar vazio")

    private String nome;

    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorMinimo;
    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer qtdHoras;
    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @Max(100)
    private BigDecimal percentagemComissao;
    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")

    private Integer horasQuarto;
    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorQuarto;
    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer horasSala;

    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorSala;

    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer horasBanheiro;


    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorBanheiro;

    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer horasCozinha;

    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    private BigDecimal valorCozinha;

    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer horasQuintal;


    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorQuintal;

    @NotEmpty(message = "Não pode estar vazio")
    @Positive(message = "O valor não poder negativo")
    private Integer horasOutros;

    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorOutros;


    private Icone icone;

    @NotEmpty(message = "Não pode estar vazio")
    @PositiveOrZero(message = "O valor não poder negativo")
    private Integer posicao;

}
