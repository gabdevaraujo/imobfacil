package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImagemDTO;
import br.com.gva.imobfacil.service.ImagemUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/imoveis/{imovelId}/imagens")
@RequiredArgsConstructor
public class ImagemController {

    private final ImagemUploadService imagemUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagemDTO> upload(
        @PathVariable Long imovelId,
        @RequestPart("arquivo") MultipartFile arquivo,
        @RequestPart(value = "descricao", required = false) String descricao) {

        ImagemDTO dto = imagemUploadService.upload(imovelId, arquivo, descricao);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{imagemId}")
    public ResponseEntity<Void> deletar(
        @PathVariable Long imovelId,
        @PathVariable Long imagemId) {

        imagemUploadService.deletarImagem(imovelId, imagemId);
        return ResponseEntity.noContent().build();
    }
}
