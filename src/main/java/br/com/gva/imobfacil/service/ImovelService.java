package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.CaracteristicaDTO;
import br.com.gva.imobfacil.dto.CorretorDTO;
import br.com.gva.imobfacil.dto.EnderecoDTO;
import br.com.gva.imobfacil.dto.ImagemDTO;
import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.dto.request.EnderecoRequest;
import br.com.gva.imobfacil.dto.request.ImovelRequest;
import br.com.gva.imobfacil.model.Caracteristica;
import br.com.gva.imobfacil.model.Corretor;
import br.com.gva.imobfacil.model.Endereco;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import br.com.gva.imobfacil.repository.CaracteristicaRepository;
import br.com.gva.imobfacil.repository.CorretorRepository;
import br.com.gva.imobfacil.repository.ImovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final CorretorRepository corretorRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final ImagemService imagemService;

    public ImovelDTO criarImovel(ImovelRequest request) {
        Imovel imovel = toEntity(request);
        return convertToDTO(imovelRepository.save(imovel));
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
        return imovelRepository.findAll(pageable).map(this::convertToDTO);
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
            return imovelRepository.findByFiltrosCompletos(
                minPreco, maxPreco, minQuartos, tipoNegocio, cidade, bairro, pageable
            ).map(this::convertToDTO);
        }
        return imovelRepository.findByFiltros(
            minPreco, maxPreco, minQuartos, tipoNegocio, pageable
        ).map(this::convertToDTO);
    }

    public Page<ImovelDTO> buscarPorTipoNegocio(TipoNegocio tipoNegocio, Pageable pageable) {
        return imovelRepository.findByTipoNegocio(tipoNegocio, pageable).map(this::convertToDTO);
    }

    public Page<ImovelDTO> buscarPorStatus(StatusImovel status, Pageable pageable) {
        return imovelRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    public Page<ImovelDTO> buscarPorCidade(String cidade, Pageable pageable) {
        return imovelRepository.findByEndereco_Cidade(cidade, pageable).map(this::convertToDTO);
    }

    public Page<ImovelDTO> buscarPorBairro(String bairro, Pageable pageable) {
        return imovelRepository.findByEndereco_Bairro(bairro, pageable).map(this::convertToDTO);
    }

    public ImovelDTO atualizarImovel(Long id, ImovelRequest request) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + id));

        imovel.setTitulo(request.getTitulo());
        imovel.setDescricao(request.getDescricao());
        imovel.setPreco(request.getPreco());
        imovel.setTipoNegocio(request.getTipoNegocio());
        imovel.setAreaTotalM2(request.getAreaTotalM2());
        imovel.setAreaPrivativaM2(request.getAreaPrivativaM2());
        imovel.setQuartos(request.getQuartos());
        imovel.setSuites(request.getSuites());
        imovel.setBanheiros(request.getBanheiros());
        imovel.setVagas(request.getVagas());
        imovel.setStatus(request.getStatus());
        imovel.setEndereco(toEndereco(request.getEndereco()));
        imovel.setCorretor(resolveCorretor(request.getCorretorId()));
        imovel.setCaracteristicas(resolveCaracteristicas(request.getCaracteristicaIds()));

        return convertToDTO(imovelRepository.save(imovel));
    }

    public void deletarImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + id));
        imagemService.deletarPorImovelId(id);
        imovelRepository.delete(imovel);
    }

    private Imovel toEntity(ImovelRequest request) {
        Imovel imovel = new Imovel();
        imovel.setReferencia(request.getReferencia());
        imovel.setTitulo(request.getTitulo());
        imovel.setDescricao(request.getDescricao());
        imovel.setPreco(request.getPreco());
        imovel.setTipoNegocio(request.getTipoNegocio());
        imovel.setAreaTotalM2(request.getAreaTotalM2());
        imovel.setAreaPrivativaM2(request.getAreaPrivativaM2());
        imovel.setQuartos(request.getQuartos());
        imovel.setSuites(request.getSuites());
        imovel.setBanheiros(request.getBanheiros());
        imovel.setVagas(request.getVagas());
        imovel.setStatus(request.getStatus());
        imovel.setEndereco(toEndereco(request.getEndereco()));
        imovel.setCorretor(resolveCorretor(request.getCorretorId()));
        imovel.setCaracteristicas(resolveCaracteristicas(request.getCaracteristicaIds()));
        return imovel;
    }

    private Endereco toEndereco(EnderecoRequest req) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(req.getLogradouro());
        endereco.setNumero(req.getNumero());
        endereco.setComplemento(req.getComplemento());
        endereco.setBairro(req.getBairro());
        endereco.setCidade(req.getCidade());
        endereco.setEstado(req.getEstado());
        endereco.setCep(req.getCep());
        endereco.setLatitude(req.getLatitude());
        endereco.setLongitude(req.getLongitude());
        return endereco;
    }

    private Corretor resolveCorretor(Long corretorId) {
        return corretorRepository.findById(corretorId)
            .orElseThrow(() -> new RuntimeException("Corretor não encontrado com ID: " + corretorId));
    }

    private List<Caracteristica> resolveCaracteristicas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return caracteristicaRepository.findAllById(ids);
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

        if (imovel.getEndereco() != null) {
            EnderecoDTO endDTO = new EnderecoDTO();
            endDTO.setId(imovel.getEndereco().getId());
            endDTO.setLogradouro(imovel.getEndereco().getLogradouro());
            endDTO.setNumero(imovel.getEndereco().getNumero());
            endDTO.setComplemento(imovel.getEndereco().getComplemento());
            endDTO.setBairro(imovel.getEndereco().getBairro());
            endDTO.setCidade(imovel.getEndereco().getCidade());
            endDTO.setEstado(imovel.getEndereco().getEstado());
            endDTO.setCep(imovel.getEndereco().getCep());
            endDTO.setLatitude(imovel.getEndereco().getLatitude());
            endDTO.setLongitude(imovel.getEndereco().getLongitude());
            dto.setEndereco(endDTO);
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
