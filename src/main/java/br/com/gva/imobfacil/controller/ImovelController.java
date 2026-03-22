package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.dto.request.ImovelRequest;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import br.com.gva.imobfacil.service.ImovelService;
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
public class ImovelController {

    private final ImovelService imovelService;

    @PostMapping
    public ResponseEntity<ImovelDTO> criarImovel(@Valid @RequestBody ImovelRequest request) {
        ImovelDTO dto = imovelService.criarImovel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelDTO> obterImovelPorId(@PathVariable Long id) {
        return ResponseEntity.ok(imovelService.obterImovelPorId(id));
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<ImovelDTO> obterImovelPorReferencia(@PathVariable String referencia) {
        return ResponseEntity.ok(imovelService.obterImovelPorReferencia(referencia));
    }

    @GetMapping
    public ResponseEntity<Page<ImovelDTO>> listarTodos(
        @RequestParam(required = false) BigDecimal minPreco,
        @RequestParam(required = false) BigDecimal maxPreco,
        @RequestParam(required = false) Integer minQuartos,
        @RequestParam(required = false) TipoNegocio tipoNegocio,
        @RequestParam(required = false) String cidade,
        @RequestParam(required = false) String bairro,
        @RequestParam(required = false) StatusImovel status,
        Pageable pageable) {

        if (minPreco != null || maxPreco != null || minQuartos != null ||
            tipoNegocio != null || cidade != null || bairro != null) {
            return ResponseEntity.ok(imovelService.buscarPorFiltros(
                minPreco != null ? minPreco : BigDecimal.ZERO,
                maxPreco != null ? maxPreco : new BigDecimal("999999999"),
                minQuartos != null ? minQuartos : 0,
                tipoNegocio,
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
    public ResponseEntity<Page<ImovelDTO>> buscarPorTipoNegocio(
        @PathVariable TipoNegocio tipoNegocio,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorTipoNegocio(tipoNegocio, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorStatus(
        @PathVariable StatusImovel status,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorStatus(status, pageable));
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorCidade(
        @PathVariable String cidade,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorCidade(cidade, pageable));
    }

    @GetMapping("/bairro/{bairro}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorBairro(
        @PathVariable String bairro,
        Pageable pageable) {
        return ResponseEntity.ok(imovelService.buscarPorBairro(bairro, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelDTO> atualizarImovel(
        @PathVariable Long id,
        @Valid @RequestBody ImovelRequest request) {
        return ResponseEntity.ok(imovelService.atualizarImovel(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable Long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}
