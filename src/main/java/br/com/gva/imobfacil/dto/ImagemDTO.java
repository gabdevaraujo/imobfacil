package br.com.gva.imobfacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagemDTO {
    private Long id;
    private String url;
    private String descricao;
    private Integer ordem;
}
