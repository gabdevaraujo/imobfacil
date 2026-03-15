package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.EnderecoDTO;
import br.com.gva.imobfacil.model.Endereco;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    
    public EnderecoDTO convertToDTO(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(endereco.getId());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        dto.setLatitude(endereco.getLatitude());
        dto.setLongitude(endereco.getLongitude());
        return dto;
    }
}
