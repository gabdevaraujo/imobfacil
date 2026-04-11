package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.dto.ImovelResumoDTO;
import br.com.gva.imobfacil.exception.GlobalExceptionHandler;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import br.com.gva.imobfacil.service.ImovelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImovelControllerTest {

    @Mock ImovelService imovelService;
    @InjectMocks ImovelController imovelController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imovelController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void shouldReturn200WithEmptyPageWhenNoImoveisExist() throws Exception {
        when(imovelService.listarTodos(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/imoveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void shouldReturn200WithResultsWhenImoveisExist() throws Exception {
        ImovelResumoDTO resumo = new ImovelResumoDTO();
        resumo.setId(1L);
        resumo.setReferencia("AP-001");
        resumo.setTitulo("Apartamento Teste");
        resumo.setPreco(new BigDecimal("500000"));

        when(imovelService.listarTodos(any())).thenReturn(new PageImpl<>(List.of(resumo)));

        mockMvc.perform(get("/imoveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].referencia").value("AP-001"));
    }

    @Test
    void shouldReturn404WhenImovelNotFound() throws Exception {
        when(imovelService.obterImovelPorId(99L))
            .thenThrow(new RecursoNaoEncontradoException("Imóvel não encontrado com ID: 99"));

        mockMvc.perform(get("/imoveis/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn201WhenImovelCreatedSuccessfully() throws Exception {
        ImovelDTO dto = new ImovelDTO();
        dto.setId(1L);
        dto.setReferencia("AP-NEW");

        when(imovelService.criarImovel(any())).thenReturn(dto);

        String body = """
            {
              "referencia": "AP-NEW",
              "titulo": "Novo Apartamento",
              "preco": 400000,
              "tipoNegocio": "VENDA",
              "tipoImovel": "APARTAMENTO",
              "areaTotalM2": 70,
              "quartos": 2,
              "status": "DISPONIVEL",
              "corretorId": 1,
              "endereco": {
                "logradouro": "Rua Teste",
                "numero": "100",
                "bairro": "Centro",
                "cidade": "São Paulo",
                "estado": "SP",
                "cep": "01000-000"
              }
            }
            """;

        mockMvc.perform(post("/imoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.referencia").value("AP-NEW"));
    }

    @Test
    void shouldReturn400WhenCreatingImovelWithBodyInvalido() throws Exception {
        mockMvc.perform(post("/imoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentImovel() throws Exception {
        org.mockito.Mockito.doThrow(new RecursoNaoEncontradoException("Imóvel não encontrado com ID: 99"))
            .when(imovelService).deletarImovel(99L);

        mockMvc.perform(delete("/imoveis/99"))
            .andExpect(status().isNotFound());
    }
}
