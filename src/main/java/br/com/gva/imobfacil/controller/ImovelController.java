package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.dto.ImovelResumoDTO;
import br.com.gva.imobfacil.dto.request.ImovelRequest;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import br.com.gva.imobfacil.service.ImovelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/imoveis")
@RequiredArgsConstructor
@Tag(name = "Imóveis", description = "Gestão do catálogo de imóveis")
public class ImovelController {

    private final ImovelService imovelService;

    @PostMapping
    @Operation(summary = "Criar imóvel", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Imóvel criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<ImovelDTO> criarImovel(@Valid @RequestBody ImovelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imovelService.criarImovel(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imóvel por ID")
    @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    public ResponseEntity<ImovelDTO> obterImovelPorId(@PathVariable Long id) {
        return ResponseEntity.ok(imovelService.obterImovelPorId(id));
    }

    @GetMapping("/referencia/{referencia}")
    @Operation(summary = "Buscar imóvel por referência (ex: AP-001)")
    @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    public ResponseEntity<ImovelDTO> obterImovelPorReferencia(@PathVariable String referencia) {
        return ResponseEntity.ok(imovelService.obterImovelPorReferencia(referencia));
    }

    @GetMapping
    @Operation(summary = "Listar / filtrar imóveis",
        description = "Retorna página de resumos. Todos os filtros são opcionais e combináveis.")
    public ResponseEntity<Page<ImovelResumoDTO>> listarTodos(
        @Parameter(description = "Preço mínimo") @RequestParam(required = false) BigDecimal minPreco,
        @Parameter(description = "Preço máximo") @RequestParam(required = false) BigDecimal maxPreco,
        @Parameter(description = "Mínimo de quartos") @RequestParam(required = false) Integer minQuartos,
        @Parameter(description = "VENDA | ALUGUEL | ALUGUEL_TEMPORADA") @RequestParam(required = false) TipoNegocio tipoNegocio,
        @Parameter(description = "APARTAMENTO | CASA | COBERTURA | STUDIO | TERRENO | COMERCIAL") @RequestParam(required = false) TipoImovel tipoImovel,
        @Parameter(description = "Cidade (ex: São Paulo)") @RequestParam(required = false) String cidade,
        @Parameter(description = "Bairro (ex: Jardins)") @RequestParam(required = false) String bairro,
        @Parameter(description = "DISPONIVEL | ALUGADO | VENDIDO | INDISPONIVEL | EM_CONSTRUCAO") @RequestParam(required = false) StatusImovel status,
        Pageable pageable) {

        if (minPreco != null || maxPreco != null || minQuartos != null ||
            tipoNegocio != null || tipoImovel != null || cidade != null || bairro != null) {
            return ResponseEntity.ok(imovelService.buscarPorFiltros(
                minPreco != null ? minPreco : BigDecimal.ZERO,
                maxPreco != null ? maxPreco : new BigDecimal("999999999"),
                minQuartos != null ? minQuartos : 0,
                tipoNegocio,
                tipoImovel,
                cidade,
                bairro,
                pageable
            ));
        } else if (status != null) {
            return ResponseEntity.ok(imovelService.buscarPorStatus(status, pageable));
        } else {
            return ResponseEntity.ok(imovelService.listarTodos(pageable));
        }
    }

    @GetMapping("/tipo/{tipoNegocio}")
    @Operation(summary = "Listar por tipo de negócio (path)")
    public ResponseEntity<Page<ImovelResumoDTO>> buscarPorTipoNegocio(
        @PathVariable TipoNegocio tipoNegocio,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorTipoNegocio(tipoNegocio, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar por status (path)")
    public ResponseEntity<Page<ImovelResumoDTO>> buscarPorStatus(
        @PathVariable StatusImovel status,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorStatus(status, pageable));
    }

    @GetMapping("/cidade/{cidade}")
    @Operation(summary = "Listar por cidade")
    public ResponseEntity<Page<ImovelResumoDTO>> buscarPorCidade(
        @PathVariable String cidade,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorCidade(cidade, pageable));
    }

    @GetMapping("/bairro/{bairro}")
    @Operation(summary = "Listar por bairro")
    public ResponseEntity<Page<ImovelResumoDTO>> buscarPorBairro(
        @PathVariable String bairro,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorBairro(bairro, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar imóvel", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    public ResponseEntity<ImovelDTO> atualizarImovel(
        @PathVariable Long id,
        @Valid @RequestBody ImovelRequest request) {
        return ResponseEntity.ok(imovelService.atualizarImovel(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir imóvel", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    public ResponseEntity<Void> deletarImovel(@PathVariable Long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}
