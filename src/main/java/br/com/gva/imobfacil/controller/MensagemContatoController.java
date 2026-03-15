package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.MensagemContatoDTO;
import br.com.gva.imobfacil.model.MensagemContato;
import br.com.gva.imobfacil.service.MensagemContatoService;
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
    public ResponseEntity<MensagemContatoDTO> criarMensagem(@RequestBody MensagemContato mensagem) {
        MensagemContatoDTO dto = mensagemContatoService.criarMensagem(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MensagemContatoDTO> obterPorId(@PathVariable Long id) {
        MensagemContatoDTO dto = mensagemContatoService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping
    public ResponseEntity<Page<MensagemContatoDTO>> listarTodas(Pageable pageable) {
        Page<MensagemContatoDTO> page = mensagemContatoService.listarTodas(pageable);
        return ResponseEntity.ok(page);
    }
    
    @GetMapping("/imovel/{imovelId}")
    public ResponseEntity<Page<MensagemContatoDTO>> listarPorImovel(
        @PathVariable Long imovelId,
        Pageable pageable) {
        Page<MensagemContatoDTO> page = mensagemContatoService.listarPorImovelId(imovelId, pageable);
        return ResponseEntity.ok(page);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long id) {
        mensagemContatoService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }
}
