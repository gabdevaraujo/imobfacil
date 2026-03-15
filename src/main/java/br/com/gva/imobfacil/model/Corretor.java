package br.com.gva.imobfacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corretor")
public class Corretor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String creci;
    
    @Column(nullable = false)
    private String telefone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private String foto;
    
    @OneToMany(mappedBy = "corretor")
    private List<Imovel> imoveis;
}
