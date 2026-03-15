package br.com.gva.imobfacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensagem_contato")
public class MensagemContato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String telefone;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;
    
    @ManyToOne
    @JoinColumn(name = "imovel_id")
    private Imovel imovel;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();
}
