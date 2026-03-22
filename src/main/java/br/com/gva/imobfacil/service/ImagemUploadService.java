package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.ImagemDTO;
import br.com.gva.imobfacil.exception.RecursoNaoEncontradoException;
import br.com.gva.imobfacil.model.Imagem;
import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.repository.ImagemRepository;
import br.com.gva.imobfacil.repository.ImovelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagemUploadService {

    private final ImagemRepository imagemRepository;
    private final ImovelRepository imovelRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public ImagemDTO upload(Long imovelId, MultipartFile arquivo, String descricao) {
        Imovel imovel = imovelRepository.findById(imovelId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Imóvel não encontrado com ID: " + imovelId));

        String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Path destino = Paths.get(uploadDir).resolve(nomeArquivo);

        try {
            Files.createDirectories(destino.getParent());
            arquivo.transferTo(destino);
        } catch (IOException e) {
            log.error("Falha ao salvar imagem. imovelId={}, arquivo={}", imovelId, nomeArquivo, e);
            throw new RuntimeException("Não foi possível salvar o arquivo de imagem.");
        }

        int proximaOrdem = proximaOrdem(imovelId);
        String url = "/uploads/imagens/" + nomeArquivo;

        Imagem imagem = new Imagem();
        imagem.setUrl(url);
        imagem.setDescricao(descricao);
        imagem.setOrdem(proximaOrdem);
        imagem.setImovel(imovel);

        Imagem salva = imagemRepository.save(imagem);
        log.info("Imagem salva. imovelId={}, imagemId={}, ordem={}", imovelId, salva.getId(), proximaOrdem);

        ImagemDTO dto = new ImagemDTO();
        dto.setId(salva.getId());
        dto.setUrl(salva.getUrl());
        dto.setDescricao(salva.getDescricao());
        dto.setOrdem(salva.getOrdem());
        return dto;
    }

    @Transactional
    public void deletarImagem(Long imovelId, Long imagemId) {
        if (!imovelRepository.existsById(imovelId)) {
            throw new RecursoNaoEncontradoException("Imóvel não encontrado com ID: " + imovelId);
        }
        Imagem imagem = imagemRepository.findById(imagemId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Imagem não encontrada com ID: " + imagemId));

        excluirArquivo(imagem.getUrl());
        imagemRepository.delete(imagem);
        reordenar(imovelId);
    }

    private int proximaOrdem(Long imovelId) {
        List<Imagem> existentes = imagemRepository.findByImovel_IdOrderByOrdem(imovelId);
        return existentes.isEmpty() ? 1 : existentes.getLast().getOrdem() + 1;
    }

    private void reordenar(Long imovelId) {
        List<Imagem> imagens = imagemRepository.findByImovel_IdOrderByOrdem(imovelId);
        for (int i = 0; i < imagens.size(); i++) {
            imagens.get(i).setOrdem(i + 1);
        }
        imagemRepository.saveAll(imagens);
    }

    private void excluirArquivo(String url) {
        try {
            String nomeArquivo = url.replaceFirst("/uploads/imagens/", "");
            Path path = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Não foi possível excluir o arquivo físico: {}", url);
        }
    }
}
