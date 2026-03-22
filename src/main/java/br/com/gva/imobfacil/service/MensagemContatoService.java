package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.dto.request.MensagemContatoRequest;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.MensagemContato;
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
public class MensagemContatoService {

    private final MensagemContatoRepository mensagemContatoRepository;
    private final ImovelRepository imovelRepository;

    public MensagemContatoDTO criarMensagem(MensagemContatoRequest request) {
        MensagemContato mensagem = toEntity(request);
        MensagemContato saved = mensagemContatoRepository.save(mensagem);
        notificarCorretor(saved);
        return convertToDTO(saved);
    }

    public MensagemContatoDTO obterPorId(Long id) {
        MensagemContato mensagem = mensagemContatoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mensagem não encontrada com ID: " + id));
        return convertToDTO(mensagem);
    }

    public Page<MensagemContatoDTO> listarTodas(Pageable pageable) {
        return mensagemContatoRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<MensagemContatoDTO> listarPorImovelId(Long imovelId, Pageable pageable) {
        return mensagemContatoRepository.findByImovel_Id(imovelId, pageable).map(this::convertToDTO);
    }

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
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com ID: " + request.getImovelId()));
            mensagem.setImovel(imovel);
        }

        return mensagem;
    }

    private void notificarCorretor(MensagemContato mensagem) {
        String corretor = mensagem.getImovel() != null ?
            mensagem.getImovel().getCorretor().getNome() : "Administrador";

        log.info("=== NOTIFICAÇÃO DE CONTATO ===");
        log.info("Para: {}", corretor);
        log.info("De: {} ({})", mensagem.getNome(), mensagem.getEmail());
        log.info("Telefone: {}", mensagem.getTelefone());
        if (mensagem.getImovel() != null) {
            log.info("Imóvel: {} - Ref: {}",
                mensagem.getImovel().getTitulo(),
                mensagem.getImovel().getReferencia());
        }
        log.info("Mensagem: {}", mensagem.getMensagem());
        log.info("Data: {}", mensagem.getDataEnvio());
        log.info("=============================");
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
        return dto;
    }
}
