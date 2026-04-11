package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CorretorDTO;
import br.com.gva.imobfacil.dto.request.CorretorRequest;
import br.com.gva.imobfacil.service.CorretorService;
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
@RequestMapping("/corretores")
@RequiredArgsConstructor
@Tag(name = "Corretores", description = "Gestão de corretores de imóveis")
public class CorretorController {

    private final CorretorService corretorService;

    @PostMapping
    @Operation(summary = "Cadastrar corretor", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Corretor cadastrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<CorretorDTO> criarCorretor(@Valid @RequestBody CorretorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(corretorService.criarCorretor(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar corretor por ID")
    @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    public ResponseEntity<CorretorDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(corretorService.obterPorId(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar corretor por e-mail")
    @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    public ResponseEntity<CorretorDTO> obterPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(corretorService.obterPorEmail(email));
    }

    @GetMapping("/creci/{creci}")
    @Operation(summary = "Buscar corretor por CRECI")
    @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    public ResponseEntity<CorretorDTO> obterPorCreci(@PathVariable String creci) {
        return ResponseEntity.ok(corretorService.obterPorCreci(creci));
    }

    @GetMapping
    @Operation(summary = "Listar todos os corretores")
    public ResponseEntity<Page<CorretorDTO>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(corretorService.listarTodos(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar corretor", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    public ResponseEntity<CorretorDTO> atualizarCorretor(
        @PathVariable Long id,
        @Valid @RequestBody CorretorRequest request) {
        return ResponseEntity.ok(corretorService.atualizarCorretor(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir corretor", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    public ResponseEntity<Void> deletarCorretor(@PathVariable Long id) {
        corretorService.deletarCorretor(id);
        return ResponseEntity.noContent().build();
    }
}
