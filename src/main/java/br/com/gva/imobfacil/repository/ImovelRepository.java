package br.com.gva.imobfacil.repository;

import br.com.gva.imobfacil.model.Imovel;
import br.com.gva.imobfacil.model.StatusImovel;
import br.com.gva.imobfacil.model.TipoNegocio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long> {
    
    Imovel findByReferencia(String referencia);
    
    Page<Imovel> findByTipoNegocio(TipoNegocio tipoNegocio, Pageable pageable);
    
    Page<Imovel> findByStatus(StatusImovel status, Pageable pageable);
    
    Page<Imovel> findByEndereco_Cidade(String cidade, Pageable pageable);
    
    Page<Imovel> findByEndereco_Bairro(String bairro, Pageable pageable);
    
    @Query("SELECT i FROM Imovel i WHERE " +
           "i.preco BETWEEN :minPreco AND :maxPreco AND " +
           "i.quartos >= :minQuartos AND " +
           "i.tipoNegocio = :tipoNegocio")
    Page<Imovel> findByFiltros(
        @Param("minPreco") BigDecimal minPreco,
        @Param("maxPreco") BigDecimal maxPreco,
        @Param("minQuartos") Integer minQuartos,
        @Param("tipoNegocio") TipoNegocio tipoNegocio,
        Pageable pageable
    );
    
    @Query("SELECT i FROM Imovel i WHERE " +
           "i.preco BETWEEN :minPreco AND :maxPreco AND " +
           "i.quartos >= :minQuartos AND " +
           "i.tipoNegocio = :tipoNegocio AND " +
           "i.endereco.cidade = :cidade AND " +
           "i.endereco.bairro = :bairro")
    Page<Imovel> findByFiltrosCompletos(
        @Param("minPreco") BigDecimal minPreco,
        @Param("maxPreco") BigDecimal maxPreco,
        @Param("minQuartos") Integer minQuartos,
        @Param("tipoNegocio") TipoNegocio tipoNegocio,
        @Param("cidade") String cidade,
        @Param("bairro") String bairro,
        Pageable pageable
    );
}
