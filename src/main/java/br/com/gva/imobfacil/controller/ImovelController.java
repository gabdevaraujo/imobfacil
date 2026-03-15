package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImovelDTO;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import br.com.gva.imobfacil.service.ImovelService;
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
    public ResponseEntity<ImovelDTO> criarImovel(@RequestBody Imovel imovel) {
        ImovelDTO dto = imovelService.criarImovel(imovel);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ImovelDTO> obterImovelPorId(@PathVariable Long id) {
        ImovelDTO dto = imovelService.obterImovelPorId(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<ImovelDTO> obterImovelPorReferencia(@PathVariable String referencia) {
        ImovelDTO dto = imovelService.obterImovelPorReferencia(referencia);
        return ResponseEntity.ok(dto);
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
        
        // Se houver filtros, aplicá-los
        if (minPreco != null || maxPreco != null || minQuartos != null || 
            tipoNegocio != null || cidade != null || bairro != null) {
            Page<ImovelDTO> page = imovelService.buscarPorFiltros(
                minPreco != null ? minPreco : BigDecimal.ZERO,
                maxPreco != null ? maxPreco : new BigDecimal("999999999"),
                minQuartos != null ? minQuartos : 0,
                tipoNegocio,
                cidade,
                bairro,
                pageable
            );
            return ResponseEntity.ok(page);
        } else if (status != null) {
            Page<ImovelDTO> page = imovelService.buscarPorStatus(status, pageable);
            return ResponseEntity.ok(page);
        } else {
            Page<ImovelDTO> page = imovelService.listarTodos(pageable);
            return ResponseEntity.ok(page);
        }
    }
    
    @GetMapping("/tipo/{tipoNegocio}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorTipoNegocio(
        @PathVariable TipoNegocio tipoNegocio,
        Pageable pageable) {
        Page<ImovelDTO> page = imovelService.buscarPorTipoNegocio(tipoNegocio, pageable);
        return ResponseEntity.ok(page);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorStatus(
        @PathVariable StatusImovel status,
        Pageable pageable) {
        Page<ImovelDTO> page = imovelService.buscarPorStatus(status, pageable);
        return ResponseEntity.ok(page);
    }
    
    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorCidade(
        @PathVariable String cidade,
        Pageable pageable) {
        Page<ImovelDTO> page = imovelService.buscarPorCidade(cidade, pageable);
        return ResponseEntity.ok(page);
    }
    
    @GetMapping("/bairro/{bairro}")
    public ResponseEntity<Page<ImovelDTO>> buscarPorBairro(
        @PathVariable String bairro,
        Pageable pageable) {
        Page<ImovelDTO> page = imovelService.buscarPorBairro(bairro, pageable);
        return ResponseEntity.ok(page);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ImovelDTO> atualizarImovel(
        @PathVariable Long id,
        @RequestBody Imovel imovelAtualizado) {
        ImovelDTO dto = imovelService.atualizarImovel(id, imovelAtualizado);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable Long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}
