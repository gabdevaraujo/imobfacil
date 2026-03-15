package br.com.gva.imobfacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imagem")
public class Imagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String url;
    
    @Column
    private String descricao;
    
    @Column(nullable = false)
    private Integer ordem;
    
    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;
}
