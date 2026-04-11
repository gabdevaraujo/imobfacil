package br.com.gva.imobfacil.dto.request;

import br.com.gva.imobfacil.model.StatusLead;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarStatusLeadRequest {

    @NotNull
    private StatusLead status;

    private String observacoes;
}
