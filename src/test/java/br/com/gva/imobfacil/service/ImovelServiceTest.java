package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.dto.ImovelResumoDTO;
import br.com.gva.imobfacil.dto.request.EnderecoRequest;
import br.com.gva.imobfacil.dto.request.ImovelRequest;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import br.com.gva.imobfacil.model.*;
import br.com.gva.imobfacil.repository.CaracteristicaRepository;
import br.com.gva.imobfacil.repository.CorretorRepository;
import br.com.gva.imobfacil.repository.ImovelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImovelServiceTest {

    @Mock ImovelRepository imovelRepository;
    @Mock CorretorRepository corretorRepository;
    @Mock CaracteristicaRepository caracteristicaRepository;
    @Mock ImagemService imagemService;
    @InjectMocks ImovelService imovelService;

    // -------------------------------------------------------------------------
    // obterImovelPorId
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnImovelDTOWhenFound() {
        Imovel imovel = buildImovel(1L);
        when(imovelRepository.findById(1L)).thenReturn(Optional.of(imovel));

        ImovelDTO result = imovelService.obterImovelPorId(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getReferencia()).isEqualTo("AP-001");
    }

    @Test
    void shouldThrowWhenImovelNotFoundById() {
        when(imovelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imovelService.obterImovelPorId(99L))
            .isInstanceOf(RecursoNaoEncontradoException.class)
            .hasMessageContaining("99");
    }

    // -------------------------------------------------------------------------
    // obterImovelPorReferencia
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowWhenReferenciaNotFound() {
        when(imovelRepository.findByReferencia("X-999")).thenReturn(null);

        assertThatThrownBy(() -> imovelService.obterImovelPorReferencia("X-999"))
            .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    // -------------------------------------------------------------------------
    // listarTodos
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnPageOfResumoWhenListingAll() {
        Imovel imovel = buildImovel(1L);
        when(imovelRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(imovel)));

        Page<ImovelResumoDTO> result = imovelService.listarTodos(Pageable.unpaged());

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getReferencia()).isEqualTo("AP-001");
    }

    // -------------------------------------------------------------------------
    // criarImovel
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowWhenCorretorNotFoundOnCreate() {
        ImovelRequest request = buildRequest(99L);
        when(corretorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imovelService.criarImovel(request))
            .isInstanceOf(RecursoNaoEncontradoException.class)
            .hasMessageContaining("Corretor");
    }

    @Test
    void shouldSaveAndReturnImovelOnCreate() {
        ImovelRequest request = buildRequest(1L);
        Corretor corretor = new Corretor();
        corretor.setId(1L);
        corretor.setNome("João Silva");

        Imovel savedImovel = buildImovel(10L);
        savedImovel.setCorretor(corretor);

        when(corretorRepository.findById(1L)).thenReturn(Optional.of(corretor));
        when(imovelRepository.save(any())).thenReturn(savedImovel);

        ImovelDTO result = imovelService.criarImovel(request);

        assertThat(result.getId()).isEqualTo(10L);
        verify(imovelRepository).save(any());
    }

    // -------------------------------------------------------------------------
    // deletarImovel
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowWhenDeletingNonExistentImovel() {
        when(imovelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imovelService.deletarImovel(99L))
            .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void shouldDeleteImovelAndItsImagens() {
        Imovel imovel = buildImovel(1L);
        when(imovelRepository.findById(1L)).thenReturn(Optional.of(imovel));

        imovelService.deletarImovel(1L);

        verify(imagemService).deletarPorImovelId(1L);
        verify(imovelRepository).delete(imovel);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Imovel buildImovel(Long id) {
        Imovel imovel = new Imovel();
        imovel.setId(id);
        imovel.setReferencia("AP-001");
        imovel.setTitulo("Apartamento Teste");
        imovel.setPreco(new BigDecimal("500000"));
        imovel.setTipoNegocio(TipoNegocio.VENDA);
        imovel.setTipoImovel(TipoImovel.APARTAMENTO);
        imovel.setQuartos(2);
        imovel.setVagas(1);
        imovel.setAreaTotalM2(new BigDecimal("80"));
        imovel.setStatus(StatusImovel.DISPONIVEL);
        imovel.setImagens(Collections.emptyList());
        imovel.setCaracteristicas(Collections.emptyList());
        return imovel;
    }

    private ImovelRequest buildRequest(Long corretorId) {
        ImovelRequest request = new ImovelRequest();
        request.setReferencia("AP-NEW");
        request.setTitulo("Novo Apartamento");
        request.setPreco(new BigDecimal("400000"));
        request.setTipoNegocio(TipoNegocio.VENDA);
        request.setTipoImovel(TipoImovel.APARTAMENTO);
        request.setAreaTotalM2(new BigDecimal("70"));
        request.setQuartos(2);
        request.setStatus(StatusImovel.DISPONIVEL);
        request.setCorretorId(corretorId);

        EnderecoRequest endereco = new EnderecoRequest();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01000-000");
        request.setEndereco(endereco);

        return request;
    }
}
