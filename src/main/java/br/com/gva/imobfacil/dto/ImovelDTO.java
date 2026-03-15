package br.com.gva.imobfacil.dto;

import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImovelDTO {
    private Long id;
    private String referencia;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private TipoNegocio tipoNegocio;
    private BigDecimal areaTotalM2;
    private BigDecimal areaPrivativaM2;
    private Integer quartos;
    private Integer suites;
    private Integer banheiros;
    private Integer vagas;
    private StatusImovel status;
    private EnderecoDTO endereco;
    private CorretorDTO corretor;
    private List<ImagemDTO> imagens;
    private List<CaracteristicaDTO> caracteristicas;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
