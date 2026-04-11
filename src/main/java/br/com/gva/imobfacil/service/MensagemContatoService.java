package br.com.gva.imobfacil.service;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import org.springframework.transaction.annotation.Transactional;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.dto.request.AtualizarStatusLeadRequest;
import br.com.gva.imobfacil.dto.request.MensagemContatoRequest;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.MensagemContato;
import br.com.gva.imobfacil.model.StatusLead;
import br.com.gva.imobfacil.repository.ImovelRepository;
import br.com.gva.imobfacil.repository.MensagemContatoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MensagemContatoService {

    private final MensagemContatoRepository mensagemContatoRepository;
    private final ImovelRepository imovelRepository;
    private final EmailService emailService;

    @Transactional
    public MensagemContatoDTO criarMensagem(MensagemContatoRequest request) {
        MensagemContato mensagem = toEntity(request);
        MensagemContato saved = mensagemContatoRepository.save(mensagem);
        emailService.enviarNotificacaoLead(saved);
        return convertToDTO(saved);
    }

    public MensagemContatoDTO obterPorId(Long id) {
        MensagemContato mensagem = mensagemContatoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Mensagem não encontrada com ID: " + id));
        return convertToDTO(mensagem);
    }

    public Page<MensagemContatoDTO> listarTodas(Pageable pageable) {
        return mensagemContatoRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<MensagemContatoDTO> listarPorStatus(StatusLead status, Pageable pageable) {
        return mensagemContatoRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    @Transactional
    public MensagemContatoDTO atualizarStatus(Long id, AtualizarStatusLeadRequest request) {
        MensagemContato mensagem = mensagemContatoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Mensagem não encontrada com ID: " + id));
        mensagem.setStatus(request.getStatus());
        if (request.getObservacoes() != null) {
            mensagem.setObservacoes(request.getObservacoes());
        }
        return convertToDTO(mensagemContatoRepository.save(mensagem));
    }

    public Page<MensagemContatoDTO> listarPorImovelId(Long imovelId, Pageable pageable) {
        return mensagemContatoRepository.findByImovel_Id(imovelId, pageable).map(this::convertToDTO);
    }

    @Transactional
    public void deletarMensagem(Long id) {
        mensagemContatoRepository.deleteById(id);
    }

    private MensagemContato toEntity(MensagemContatoRequest request) {
        MensagemContato mensagem = new MensagemContato();
        mensagem.setNome(request.getNome());
        mensagem.setEmail(request.getEmail());
        mensagem.setTelefone(request.getTelefone());
        mensagem.setMensagem(request.getMensagem());

        if (request.getImovelId() != null) {
            Imovel imovel = imovelRepository.findById(request.getImovelId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Imóvel não encontrado com ID: " + request.getImovelId()));
            mensagem.setImovel(imovel);
        }

        return mensagem;
    }

    public MensagemContatoDTO convertToDTO(MensagemContato mensagem) {
        MensagemContatoDTO dto = new MensagemContatoDTO();
        dto.setId(mensagem.getId());
        dto.setNome(mensagem.getNome());
        dto.setEmail(mensagem.getEmail());
        dto.setTelefone(mensagem.getTelefone());
        dto.setMensagem(mensagem.getMensagem());
        dto.setImovelId(mensagem.getImovel() != null ? mensagem.getImovel().getId() : null);
        dto.setDataEnvio(mensagem.getDataEnvio());
        dto.setStatus(mensagem.getStatus());
        dto.setObservacoes(mensagem.getObservacoes());
        return dto;
    }
}
