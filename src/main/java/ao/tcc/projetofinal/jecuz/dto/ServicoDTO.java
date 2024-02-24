package ao.tcc.projetofinal.jecuz.dto;

import jakarta.validation.constraints.*;
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
    @NotNull
    @Size(min = 3 , max = 50)
    private String nome;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorMinimo;

    @NotNull
    @PositiveOrZero
    private Integer qtdHoras;

    @NotNull
    @PositiveOrZero
    @Max(100)
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal percentagemComissao;

    @NotNull
    @PositiveOrZero
    private Integer horasQuarto;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorQuarto;

    @NotNull
    @PositiveOrZero
    private Integer horasSala;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorSala;

    @NotNull
    @PositiveOrZero
    private Integer horasBanheiro;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorBanheiro;

    @NotNull
    @PositiveOrZero
    private Integer horasCozinha;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorCozinha;

    @NotNull
    @PositiveOrZero
    private Integer horasQuintal;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorQuintal;

    @NotNull
    @PositiveOrZero
    private Integer horasOutros;

    @NotNull
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal valorOutros;
    /*@NotNull
    private Icone icone;*/

    @NotNull
    @Positive
    private Integer posicao;
}
