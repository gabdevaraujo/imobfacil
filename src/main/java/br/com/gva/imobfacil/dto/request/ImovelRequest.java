package br.com.gva.imobfacil.dto.request;

import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImovelRequest {

    @NotBlank
    private String referencia;

    @NotBlank
    private String titulo;

    private String descricao;

    @NotNull
    @Positive
    private BigDecimal preco;

    @NotNull
    private TipoNegocio tipoNegocio;

    @NotNull
    private TipoImovel tipoImovel;

    @NotNull
    @Positive
    private BigDecimal areaTotalM2;

    @Positive
    private BigDecimal areaPrivativaM2;

    @NotNull
    @Min(0)
    private Integer quartos;

    @Min(0)
    private Integer suites;

    @Min(0)
    private Integer banheiros;

    @Min(0)
    private Integer vagas;

    @NotNull
    private StatusImovel status;

    @NotNull
    @Valid
    private EnderecoRequest endereco;

    @NotNull
    private Long corretorId;

    private List<Long> caracteristicaIds;
}
