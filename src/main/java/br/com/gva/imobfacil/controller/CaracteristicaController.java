package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CaracteristicaDTO;
import br.com.gva.imobfacil.dto.request.CaracteristicaRequest;
import br.com.gva.imobfacil.service.CaracteristicaService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/caracteristicas")
@RequiredArgsConstructor
@Tag(name = "Características", description = "Catálogo de características de imóveis (ex: Piscina, Portaria 24h)")
public class CaracteristicaController {

    private final CaracteristicaService caracteristicaService;

    @PostMapping
    @Operation(summary = "Criar característica", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Característica criada")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<CaracteristicaDTO> criarCaracteristica(@Valid @RequestBody CaracteristicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caracteristicaService.criarCaracteristica(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar característica por ID")
    @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    public ResponseEntity<CaracteristicaDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caracteristicaService.obterPorId(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar característica por nome")
    @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    public ResponseEntity<CaracteristicaDTO> obterPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(caracteristicaService.obterPorNome(nome));
    }

    @GetMapping
    @Operation(summary = "Listar todas as características")
    public ResponseEntity<Page<CaracteristicaDTO>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(caracteristicaService.listarTodas(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar característica", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    public ResponseEntity<CaracteristicaDTO> atualizarCaracteristica(
        @PathVariable Long id,
        @Valid @RequestBody CaracteristicaRequest request) {
        return ResponseEntity.ok(caracteristicaService.atualizarCaracteristica(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir característica", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Excluída com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    public ResponseEntity<Void> deletarCaracteristica(@PathVariable Long id) {
        caracteristicaService.deletarCaracteristica(id);
        return ResponseEntity.noContent().build();
    }
}
