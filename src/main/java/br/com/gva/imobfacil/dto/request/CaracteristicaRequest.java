package br.com.gva.imobfacil.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaracteristicaRequest {

    @NotBlank
    private String nome;
}
