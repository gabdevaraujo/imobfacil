package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.ImagemDTO;
import br.com.gva.imobfacil.service.ImagemUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/imoveis/{imovelId}/imagens")
@RequiredArgsConstructor
@Tag(name = "Imagens", description = "Upload e remoção de imagens de imóveis")
public class ImagemController {

    private final ImagemUploadService imagemUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de imagem",
        description = "Salva o arquivo em disco e vincula ao imóvel com a próxima ordem disponível",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Imagem salva")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    public ResponseEntity<ImagemDTO> upload(
        @PathVariable Long imovelId,
        @RequestPart("arquivo") MultipartFile arquivo,
        @RequestPart(value = "descricao", required = false) String descricao) {

        ImagemDTO dto = imagemUploadService.upload(imovelId, arquivo, descricao);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{imagemId}")
    @Operation(summary = "Excluir imagem",
        description = "Remove o arquivo físico e reordena as imagens restantes",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Imagem excluída")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Imagem não encontrada")
    public ResponseEntity<Void> deletar(
        @PathVariable Long imovelId,
        @PathVariable Long imagemId) {

        imagemUploadService.deletarImagem(imovelId, imagemId);
        return ResponseEntity.noContent().build();
    }
}
