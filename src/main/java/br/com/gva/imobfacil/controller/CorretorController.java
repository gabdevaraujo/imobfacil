package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CorretorDTO;
import br.com.gva.imobfacil.dto.request.CorretorRequest;
import br.com.gva.imobfacil.service.CorretorService;
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
public class CorretorController {

    private final CorretorService corretorService;

    @PostMapping
    public ResponseEntity<CorretorDTO> criarCorretor(@Valid @RequestBody CorretorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(corretorService.criarCorretor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorretorDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(corretorService.obterPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CorretorDTO> obterPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(corretorService.obterPorEmail(email));
    }

    @GetMapping("/creci/{creci}")
    public ResponseEntity<CorretorDTO> obterPorCreci(@PathVariable String creci) {
        return ResponseEntity.ok(corretorService.obterPorCreci(creci));
    }

    @GetMapping
    public ResponseEntity<Page<CorretorDTO>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(corretorService.listarTodos(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorretorDTO> atualizarCorretor(
        @PathVariable Long id,
        @Valid @RequestBody CorretorRequest request) {
        return ResponseEntity.ok(corretorService.atualizarCorretor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCorretor(@PathVariable Long id) {
        corretorService.deletarCorretor(id);
        return ResponseEntity.noContent().build();
    }
}
