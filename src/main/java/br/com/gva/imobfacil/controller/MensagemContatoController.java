package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.dto.request.MensagemContatoRequest;
import br.com.gva.imobfacil.service.MensagemContatoService;
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
public class MensagemContatoController {

    private final MensagemContatoService mensagemContatoService;

    @PostMapping
    public ResponseEntity<MensagemContatoDTO> criarMensagem(@Valid @RequestBody MensagemContatoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemContatoService.criarMensagem(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensagemContatoDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mensagemContatoService.obterPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<MensagemContatoDTO>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(mensagemContatoService.listarTodas(pageable));
    }

    @GetMapping("/imovel/{imovelId}")
    public ResponseEntity<Page<MensagemContatoDTO>> listarPorImovel(
        @PathVariable Long imovelId,
        Pageable pageable) {
        return ResponseEntity.ok(mensagemContatoService.listarPorImovelId(imovelId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long id) {
        mensagemContatoService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }
}
