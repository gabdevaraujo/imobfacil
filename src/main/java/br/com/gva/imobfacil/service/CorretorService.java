package br.com.gva.imobfacil.service;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import org.springframework.transaction.annotation.Transactional;

import br.com.gva.imobfacil.dto.CorretorDTO;
import br.com.gva.imobfacil.dto.request.CorretorRequest;
import br.com.gva.imobfacil.model.Corretor;
import br.com.gva.imobfacil.repository.CorretorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CorretorService {
    
    private final CorretorRepository corretorRepository;
    
    @Transactional
    public CorretorDTO criarCorretor(CorretorRequest request) {
        Corretor saved = corretorRepository.save(toEntity(request));
        return convertToDTO(saved);
    }
    
    public CorretorDTO obterPorId(Long id) {
        Corretor corretor = corretorRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Corretor não encontrado com ID: " + id));
        return convertToDTO(corretor);
    }
    
    public CorretorDTO obterPorEmail(String email) {
        Corretor corretor = corretorRepository.findByEmail(email);
        if (corretor == null) {
            throw new RecursoNaoEncontradoException("Corretor não encontrado com email: " + email);
        }
        return convertToDTO(corretor);
    }
    
    public CorretorDTO obterPorCreci(String creci) {
        Corretor corretor = corretorRepository.findByCreci(creci);
        if (corretor == null) {
            throw new RecursoNaoEncontradoException("Corretor não encontrado com CRECI: " + creci);
        }
        return convertToDTO(corretor);
    }
    
    public Page<CorretorDTO> listarTodos(Pageable pageable) {
        return corretorRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    @Transactional
    public CorretorDTO atualizarCorretor(Long id, CorretorRequest request) {
        Corretor corretor = corretorRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Corretor não encontrado com ID: " + id));

        corretor.setNome(request.getNome());
        corretor.setCreci(request.getCreci());
        corretor.setTelefone(request.getTelefone());
        corretor.setEmail(request.getEmail());
        corretor.setFoto(request.getFoto());

        return convertToDTO(corretorRepository.save(corretor));
    }

    private Corretor toEntity(CorretorRequest request) {
        Corretor corretor = new Corretor();
        corretor.setNome(request.getNome());
        corretor.setCreci(request.getCreci());
        corretor.setTelefone(request.getTelefone());
        corretor.setEmail(request.getEmail());
        corretor.setFoto(request.getFoto());
        return corretor;
    }
    
    @Transactional
    public void deletarCorretor(Long id) {
        corretorRepository.deleteById(id);
    }
    
    public CorretorDTO convertToDTO(Corretor corretor) {
        CorretorDTO dto = new CorretorDTO();
        dto.setId(corretor.getId());
        dto.setNome(corretor.getNome());
        dto.setCreci(corretor.getCreci());
        dto.setTelefone(corretor.getTelefone());
        dto.setEmail(corretor.getEmail());
        dto.setFoto(corretor.getFoto());
        return dto;
    }
}
