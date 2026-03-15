package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CaracteristicaDTO;
import br.com.gva.imobfacil.model.Caracteristica;
import br.com.gva.imobfacil.service.CaracteristicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caracteristicas")
@RequiredArgsConstructor
public class CaracteristicaController {
    
    private final CaracteristicaService caracteristicaService;
    
    @PostMapping
    public ResponseEntity<CaracteristicaDTO> criarCaracteristica(@RequestBody Caracteristica caracteristica) {
        CaracteristicaDTO dto = caracteristicaService.criarCaracteristica(caracteristica);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> obterPorId(@PathVariable Long id) {
        CaracteristicaDTO dto = caracteristicaService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/nome/{nome}")
    public ResponseEntity<CaracteristicaDTO> obterPorNome(@PathVariable String nome) {
        CaracteristicaDTO dto = caracteristicaService.obterPorNome(nome);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping
    public ResponseEntity<Page<CaracteristicaDTO>> listarTodas(Pageable pageable) {
        Page<CaracteristicaDTO> page = caracteristicaService.listarTodas(pageable);
        return ResponseEntity.ok(page);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> atualizarCaracteristica(
        @PathVariable Long id,
        @RequestBody Caracteristica caracteristicaAtualizada) {
        CaracteristicaDTO dto = caracteristicaService.atualizarCaracteristica(id, caracteristicaAtualizada);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCaracteristica(@PathVariable Long id) {
        caracteristicaService.deletarCaracteristica(id);
        return ResponseEntity.noContent().build();
    }
}
