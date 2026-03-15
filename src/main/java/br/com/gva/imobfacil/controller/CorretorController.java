package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CorretorDTO;
import br.com.gva.imobfacil.model.Corretor;
import br.com.gva.imobfacil.service.CorretorService;
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
    public ResponseEntity<CorretorDTO> criarCorretor(@RequestBody Corretor corretor) {
        CorretorDTO dto = corretorService.criarCorretor(corretor);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CorretorDTO> obterPorId(@PathVariable Long id) {
        CorretorDTO dto = corretorService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<CorretorDTO> obterPorEmail(@PathVariable String email) {
        CorretorDTO dto = corretorService.obterPorEmail(email);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/creci/{creci}")
    public ResponseEntity<CorretorDTO> obterPorCreci(@PathVariable String creci) {
        CorretorDTO dto = corretorService.obterPorCreci(creci);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping
    public ResponseEntity<Page<CorretorDTO>> listarTodos(Pageable pageable) {
        Page<CorretorDTO> page = corretorService.listarTodos(pageable);
        return ResponseEntity.ok(page);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CorretorDTO> atualizarCorretor(
        @PathVariable Long id,
        @RequestBody Corretor corretorAtualizado) {
        CorretorDTO dto = corretorService.atualizarCorretor(id, corretorAtualizado);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCorretor(@PathVariable Long id) {
        corretorService.deletarCorretor(id);
        return ResponseEntity.noContent().build();
    }
}
