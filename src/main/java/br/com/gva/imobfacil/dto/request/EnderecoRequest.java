package br.com.gva.imobfacil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoRequest {

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    @Size(min = 2, max = 2, message = "Estado deve ter exatamente 2 caracteres")
    private String estado;

    @NotBlank
    private String cep;

    private Double latitude;

    private Double longitude;
}
