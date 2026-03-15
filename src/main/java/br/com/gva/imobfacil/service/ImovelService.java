package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.*;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import br.com.gva.imobfacil.repository.ImovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImovelService {
    
    private final ImovelRepository imovelRepository;
    private final ImagemService imagemService;
    
    public ImovelDTO criarImovel(Imovel imovel) {
        Imovel saved = imovelRepository.save(imovel);
        return convertToDTO(saved);
    }
    
    public ImovelDTO obterImovelPorId(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + id));
        return convertToDTO(imovel);
    }
    
    public ImovelDTO obterImovelPorReferencia(String referencia) {
        Imovel imovel = imovelRepository.findByReferencia(referencia);
        if (imovel == null) {
            throw new RuntimeException("Imóvel não encontrado com referência: " + referencia);
        }
        return convertToDTO(imovel);
    }
    
    public Page<ImovelDTO> listarTodos(Pageable pageable) {
        Page<Imovel> page = imovelRepository.findAll(pageable);
        return page.map(this::convertToDTO);
    }
    
    public Page<ImovelDTO> buscarPorFiltros(
        BigDecimal minPreco,
        BigDecimal maxPreco,
        Integer minQuartos,
        TipoNegocio tipoNegocio,
        String cidade,
        String bairro,
        Pageable pageable) {
        
        if (cidade != null && bairro != null) {
            Page<Imovel> page = imovelRepository.findByFiltrosCompletos(
                minPreco, maxPreco, minQuartos, tipoNegocio, cidade, bairro, pageable
            );
            return page.map(this::convertToDTO);
        } else {
            Page<Imovel> page = imovelRepository.findByFiltros(
                minPreco, maxPreco, minQuartos, tipoNegocio, pageable
            );
            return page.map(this::convertToDTO);
        }
    }
    
    public Page<ImovelDTO> buscarPorTipoNegocio(TipoNegocio tipoNegocio, Pageable pageable) {
        Page<Imovel> page = imovelRepository.findByTipoNegocio(tipoNegocio, pageable);
        return page.map(this::convertToDTO);
    }
    
    public Page<ImovelDTO> buscarPorStatus(StatusImovel status, Pageable pageable) {
        Page<Imovel> page = imovelRepository.findByStatus(status, pageable);
        return page.map(this::convertToDTO);
    }
    
    public Page<ImovelDTO> buscarPorCidade(String cidade, Pageable pageable) {
        Page<Imovel> page = imovelRepository.findByEndereco_Cidade(cidade, pageable);
        return page.map(this::convertToDTO);
    }
    
    public Page<ImovelDTO> buscarPorBairro(String bairro, Pageable pageable) {
        Page<Imovel> page = imovelRepository.findByEndereco_Bairro(bairro, pageable);
        return page.map(this::convertToDTO);
    }
    
    public ImovelDTO atualizarImovel(Long id, Imovel imovelAtualizado) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + id));
        
        imovel.setTitulo(imovelAtualizado.getTitulo());
        imovel.setDescricao(imovelAtualizado.getDescricao());
        imovel.setPreco(imovelAtualizado.getPreco());
        imovel.setTipoNegocio(imovelAtualizado.getTipoNegocio());
        imovel.setAreaTotalM2(imovelAtualizado.getAreaTotalM2());
        imovel.setAreaPrivativaM2(imovelAtualizado.getAreaPrivativaM2());
        imovel.setQuartos(imovelAtualizado.getQuartos());
        imovel.setSuites(imovelAtualizado.getSuites());
        imovel.setBanheiros(imovelAtualizado.getBanheiros());
        imovel.setVagas(imovelAtualizado.getVagas());
        imovel.setStatus(imovelAtualizado.getStatus());
        
        if (imovelAtualizado.getEndereco() != null) {
            imovel.setEndereco(imovelAtualizado.getEndereco());
        }
        
        if (imovelAtualizado.getCorretor() != null) {
            imovel.setCorretor(imovelAtualizado.getCorretor());
        }
        
        Imovel updated = imovelRepository.save(imovel);
        return convertToDTO(updated);
    }
    
    public void deletarImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + id));
        
        imagemService.deletarPorImovelId(id);
        imovelRepository.delete(imovel);
    }
    
    private ImovelDTO convertToDTO(Imovel imovel) {
        ImovelDTO dto = new ImovelDTO();
        dto.setId(imovel.getId());
        dto.setReferencia(imovel.getReferencia());
        dto.setTitulo(imovel.getTitulo());
        dto.setDescricao(imovel.getDescricao());
        dto.setPreco(imovel.getPreco());
        dto.setTipoNegocio(imovel.getTipoNegocio());
        dto.setAreaTotalM2(imovel.getAreaTotalM2());
        dto.setAreaPrivativaM2(imovel.getAreaPrivativaM2());
        dto.setQuartos(imovel.getQuartos());
        dto.setSuites(imovel.getSuites());
        dto.setBanheiros(imovel.getBanheiros());
        dto.setVagas(imovel.getVagas());
        dto.setStatus(imovel.getStatus());
        dto.setDataCriacao(imovel.getDataCriacao());
        dto.setDataAtualizacao(imovel.getDataAtualizacao());
        
        // Converter relacionamentos
        if (imovel.getEndereco() != null) {
            EnderecoService enderecoService = new EnderecoService();
            dto.setEndereco(enderecoService.convertToDTO(imovel.getEndereco()));
        }
        
        if (imovel.getCorretor() != null) {
            CorretorDTO corretorDTO = new CorretorDTO();
            corretorDTO.setId(imovel.getCorretor().getId());
            corretorDTO.setNome(imovel.getCorretor().getNome());
            corretorDTO.setCreci(imovel.getCorretor().getCreci());
            corretorDTO.setTelefone(imovel.getCorretor().getTelefone());
            corretorDTO.setEmail(imovel.getCorretor().getEmail());
            corretorDTO.setFoto(imovel.getCorretor().getFoto());
            dto.setCorretor(corretorDTO);
        }
        
        if (imovel.getImagens() != null) {
            dto.setImagens(imovel.getImagens().stream()
                .map(img -> {
                    ImagemDTO imgDTO = new ImagemDTO();
                    imgDTO.setId(img.getId());
                    imgDTO.setUrl(img.getUrl());
                    imgDTO.setDescricao(img.getDescricao());
                    imgDTO.setOrdem(img.getOrdem());
                    return imgDTO;
                })
                .collect(Collectors.toList()));
        }
        
        if (imovel.getCaracteristicas() != null) {
            dto.setCaracteristicas(imovel.getCaracteristicas().stream()
                .map(car -> {
                    CaracteristicaDTO carDTO = new CaracteristicaDTO();
                    carDTO.setId(car.getId());
                    carDTO.setNome(car.getNome());
                    return carDTO;
                })
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
