package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.CaracteristicaDTO;
import br.com.gva.imobfacil.dto.request.CaracteristicaRequest;
import br.com.gva.imobfacil.model.Caracteristica;
import br.com.gva.imobfacil.repository.CaracteristicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaracteristicaService {
    
    private final CaracteristicaRepository caracteristicaRepository;
    
    public CaracteristicaDTO criarCaracteristica(CaracteristicaRequest request) {
        Caracteristica saved = caracteristicaRepository.save(toEntity(request));
        return convertToDTO(saved);
    }

    private Caracteristica toEntity(CaracteristicaRequest request) {
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNome(request.getNome());
        return caracteristica;
    }
    
    public CaracteristicaDTO obterPorId(Long id) {
        Caracteristica caracteristica = caracteristicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Característica não encontrada com ID: " + id));
        return convertToDTO(caracteristica);
    }
    
    public CaracteristicaDTO obterPorNome(String nome) {
        Caracteristica caracteristica = caracteristicaRepository.findByNome(nome);
        if (caracteristica == null) {
            throw new RuntimeException("Característica não encontrada com nome: " + nome);
        }
        return convertToDTO(caracteristica);
    }
    
    public Page<CaracteristicaDTO> listarTodas(Pageable pageable) {
        return caracteristicaRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    public CaracteristicaDTO atualizarCaracteristica(Long id, CaracteristicaRequest request) {
        Caracteristica caracteristica = caracteristicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Característica não encontrada com ID: " + id));

        caracteristica.setNome(request.getNome());

        return convertToDTO(caracteristicaRepository.save(caracteristica));
    }
    
    public void deletarCaracteristica(Long id) {
        caracteristicaRepository.deleteById(id);
    }
    
    public CaracteristicaDTO convertToDTO(Caracteristica caracteristica) {
        CaracteristicaDTO dto = new CaracteristicaDTO();
        dto.setId(caracteristica.getId());
        dto.setNome(caracteristica.getNome());
        return dto;
    }
}
