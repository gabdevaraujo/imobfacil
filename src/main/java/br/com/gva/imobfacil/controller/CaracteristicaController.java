package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.CaracteristicaDTO;
import br.com.gva.imobfacil.dto.request.CaracteristicaRequest;
import br.com.gva.imobfacil.service.CaracteristicaService;
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
public class CaracteristicaController {

    private final CaracteristicaService caracteristicaService;

    @PostMapping
    public ResponseEntity<CaracteristicaDTO> criarCaracteristica(@Valid @RequestBody CaracteristicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caracteristicaService.criarCaracteristica(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caracteristicaService.obterPorId(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<CaracteristicaDTO> obterPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(caracteristicaService.obterPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<Page<CaracteristicaDTO>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(caracteristicaService.listarTodas(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> atualizarCaracteristica(
        @PathVariable Long id,
        @Valid @RequestBody CaracteristicaRequest request) {
        return ResponseEntity.ok(caracteristicaService.atualizarCaracteristica(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCaracteristica(@PathVariable Long id) {
        caracteristicaService.deletarCaracteristica(id);
        return ResponseEntity.noContent().build();
    }
}
