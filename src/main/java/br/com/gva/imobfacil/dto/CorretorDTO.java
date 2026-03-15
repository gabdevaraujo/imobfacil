package br.com.gva.imobfacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorretorDTO {
    private Long id;
    private String nome;
    private String creci;
    private String telefone;
    private String email;
    private String foto;
}
