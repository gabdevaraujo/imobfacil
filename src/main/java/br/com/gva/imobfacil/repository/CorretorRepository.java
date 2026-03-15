package br.com.gva.imobfacil.repository;

import br.com.gva.imobfacil.model.Corretor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, Long> {
    
    Corretor findByEmail(String email);
    
    Corretor findByCreci(String creci);
}
