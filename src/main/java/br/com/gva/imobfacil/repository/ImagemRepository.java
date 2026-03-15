package br.com.gva.imobfacil.repository;

import br.com.gva.imobfacil.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    
    List<Imagem> findByImovel_IdOrderByOrdem(Long imovelId);
    
    void deleteByImovel_Id(Long imovelId);
}
