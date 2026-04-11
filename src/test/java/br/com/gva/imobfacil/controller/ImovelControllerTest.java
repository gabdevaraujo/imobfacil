package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImovelResumoDTO;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import br.com.gva.imobfacil.service.ImovelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ImovelControllerTest {

    @Autowired WebApplicationContext context;
    @MockitoBean ImovelService imovelService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    void shouldReturn200WithEmptyPageWhenNoImoveisExist() throws Exception {
        when(imovelService.listarTodos(any()))
            .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/imoveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void shouldReturn200WithResultsWhenImoveisExist() throws Exception {
        ImovelResumoDTO resumo = new ImovelResumoDTO();
        resumo.setId(1L);
        resumo.setReferencia("AP-001");
        resumo.setPreco(new BigDecimal("500000"));

        when(imovelService.listarTodos(any()))
            .thenReturn(new PageImpl<>(List.of(resumo), PageRequest.of(0, 10), 1));

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
    void shouldReturn401WhenCreatingImovelSemToken() throws Exception {
        mockMvc.perform(post("/imoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Teste\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenCreatingImovelWithBodyInvalido() throws Exception {
        mockMvc.perform(post("/imoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenDeletingImovelSemToken() throws Exception {
        mockMvc.perform(delete("/imoveis/1"))
            .andExpect(status().isUnauthorized());
    }
}
