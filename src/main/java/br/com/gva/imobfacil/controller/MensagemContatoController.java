package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.dto.request.AtualizarStatusLeadRequest;
import br.com.gva.imobfacil.dto.request.MensagemContatoRequest;
import br.com.gva.imobfacil.model.StatusLead;
import br.com.gva.imobfacil.service.MensagemContatoService;
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

@RestController
@RequestMapping("/contato")
@RequiredArgsConstructor
@Tag(name = "Contato", description = "Captura pública de leads e gestão administrativa de mensagens")
public class MensagemContatoController {

    private final MensagemContatoService mensagemContatoService;

    @PostMapping
    @Operation(summary = "Enviar mensagem de interesse",
        description = "Endpoint público — não requer autenticação. imovelId é opcional.")
    @ApiResponse(responseCode = "201", description = "Mensagem registrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<MensagemContatoDTO> criarMensagem(@Valid @RequestBody MensagemContatoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemContatoService.criarMensagem(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Mensagem não encontrada")
    public ResponseEntity<MensagemContatoDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mensagemContatoService.obterPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar mensagens com filtro opcional por status",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Page<MensagemContatoDTO>> listarTodas(
        @Parameter(description = "NOVO | EM_CONTATO | VISITA_AGENDADA | FECHADO | DESCARTADO")
        @RequestParam(required = false) StatusLead status,
        Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(mensagemContatoService.listarPorStatus(status, pageable));
        }
        return ResponseEntity.ok(mensagemContatoService.listarTodas(pageable));
    }

    @GetMapping("/imovel/{imovelId}")
    @Operation(summary = "Listar mensagens por imóvel", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Page<MensagemContatoDTO>> listarPorImovel(
        @PathVariable Long imovelId,
        Pageable pageable) {
        return ResponseEntity.ok(mensagemContatoService.listarPorImovelId(imovelId, pageable));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do lead",
        description = "Avança ou retrocede o status do lead no ciclo de vida. observacoes é opcional.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Status atualizado")
    @ApiResponse(responseCode = "400", description = "Status inválido")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Mensagem não encontrada")
    public ResponseEntity<MensagemContatoDTO> atualizarStatus(
        @PathVariable Long id,
        @Valid @RequestBody AtualizarStatusLeadRequest request) {
        return ResponseEntity.ok(mensagemContatoService.atualizarStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir mensagem", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Excluída com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long id) {
        mensagemContatoService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }
}
