package br.com.gva.imobfacil.dto;

import br.com.gva.imobfacil.model.StatusLead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemContatoDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String mensagem;
    private Long imovelId;
    private LocalDateTime dataEnvio;
    private StatusLead status;
    private String observacoes;
}
