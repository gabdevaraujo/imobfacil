package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.service.EmailService;
import br.com.gva.imobfacil.dto.request.MensagemContatoRequest;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import br.com.gva.imobfacil.model.MensagemContato;
import br.com.gva.imobfacil.repository.ImovelRepository;
import br.com.gva.imobfacil.repository.MensagemContatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensagemContatoServiceTest {

    @Mock MensagemContatoRepository mensagemContatoRepository;
    @Mock ImovelRepository imovelRepository;
    @Mock EmailService emailService;
    @InjectMocks MensagemContatoService mensagemContatoService;

    @Test
    void shouldSaveMensagemSemImovelVinculado() {
        MensagemContatoRequest request = buildRequest(null);

        MensagemContato saved = buildMensagem(1L);
        when(mensagemContatoRepository.save(any())).thenReturn(saved);

        MensagemContatoDTO result = mensagemContatoService.criarMensagem(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Maria Oliveira");
        assertThat(result.getImovelId()).isNull();
        verify(mensagemContatoRepository).save(any());
    }

    @Test
    void shouldThrowWhenImovelVinculadoNaoExiste() {
        MensagemContatoRequest request = buildRequest(99L);
        when(imovelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemContatoService.criarMensagem(request))
            .isInstanceOf(RecursoNaoEncontradoException.class)
            .hasMessageContaining("Imóvel");
    }

    @Test
    void shouldThrowWhenMensagemNaoEncontradaPorId() {
        when(mensagemContatoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemContatoService.obterPorId(99L))
            .isInstanceOf(RecursoNaoEncontradoException.class)
            .hasMessageContaining("99");
    }

    @Test
    void shouldReturnMensagemWhenFoundById() {
        MensagemContato mensagem = buildMensagem(5L);
        when(mensagemContatoRepository.findById(5L)).thenReturn(Optional.of(mensagem));

        MensagemContatoDTO result = mensagemContatoService.obterPorId(5L);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getEmail()).isEqualTo("maria@teste.com");
    }

    @Test
    void shouldDeleteMensagem() {
        mensagemContatoService.deletarMensagem(1L);
        verify(mensagemContatoRepository).deleteById(1L);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private MensagemContatoRequest buildRequest(Long imovelId) {
        MensagemContatoRequest request = new MensagemContatoRequest();
        request.setNome("Maria Oliveira");
        request.setEmail("maria@teste.com");
        request.setTelefone("(11) 99999-0001");
        request.setMensagem("Tenho interesse no imóvel.");
        request.setImovelId(imovelId);
        return request;
    }

    private MensagemContato buildMensagem(Long id) {
        MensagemContato mensagem = new MensagemContato();
        mensagem.setId(id);
        mensagem.setNome("Maria Oliveira");
        mensagem.setEmail("maria@teste.com");
        mensagem.setTelefone("(11) 99999-0001");
        mensagem.setMensagem("Tenho interesse no imóvel.");
        return mensagem;
    }
}
