package br.com.gva.imobfacil.repository;

import br.com.gva.imobfacil.model.MensagemContato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemContatoRepository extends JpaRepository<MensagemContato, Long> {
    
    Page<MensagemContato> findByImovel_Id(Long imovelId, Pageable pageable);
}
