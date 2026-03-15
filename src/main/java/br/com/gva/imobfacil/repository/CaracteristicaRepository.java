package br.com.gva.imobfacil.repository;

import br.com.gva.imobfacil.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    
    Caracteristica findByNome(String nome);
}
