package br.com.gva.imobfacil.dto;

import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImovelResumoDTO {

    private Long id;
    private String referencia;
    private String titulo;
    private BigDecimal preco;
    private TipoNegocio tipoNegocio;
    private TipoImovel tipoImovel;
    private Integer quartos;
    private Integer vagas;
    private BigDecimal areaTotalM2;
    private StatusImovel status;

    // Thumbnail: URL da imagem de menor ordem
    private String thumbnail;

    // Localização básica
    private String cidade;
    private String bairro;
    private String estado;
}
