package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.ImagemDTO;
import br.com.gva.imobfacil.model.Imagem;
import br.com.gva.imobfacil.repository.ImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImagemService {
    
    private final ImagemRepository imagemRepository;
    
    public ImagemDTO criarImagem(Imagem imagem) {
        Imagem saved = imagemRepository.save(imagem);
        return convertToDTO(saved);
    }
    
    public List<ImagemDTO> obterPorImovelId(Long imovelId) {
        return imagemRepository.findByImovel_IdOrderByOrdem(imovelId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public void deletarPorImovelId(Long imovelId) {
        imagemRepository.deleteByImovel_Id(imovelId);
    }
    
    public void deletarImagem(Long id) {
        imagemRepository.deleteById(id);
    }
    
    public ImagemDTO convertToDTO(Imagem imagem) {
        ImagemDTO dto = new ImagemDTO();
        dto.setId(imagem.getId());
        dto.setUrl(imagem.getUrl());
        dto.setDescricao(imagem.getDescricao());
        dto.setOrdem(imagem.getOrdem());
        return dto;
    }
}
