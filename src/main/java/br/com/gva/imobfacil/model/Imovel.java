package br.com.gva.imobfacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imovel")
public class Imovel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String referencia;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNegocio tipoNegocio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImovel tipoImovel;
    
    @Column(nullable = false)
    private BigDecimal areaTotalM2;
    
    @Column
    private BigDecimal areaPrivativaM2;
    
    @Column(nullable = false)
    private Integer quartos;
    
    @Column
    private Integer suites;
    
    @Column
    private Integer banheiros;
    
    @Column
    private Integer vagas;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusImovel status;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;
    
    @ManyToOne
    @JoinColumn(name = "corretor_id", nullable = false)
    private Corretor corretor;
    
    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens;
    
    @ManyToMany
    @JoinTable(
        name = "imovel_caracteristica",
        joinColumns = @JoinColumn(name = "imovel_id"),
        inverseJoinColumns = @JoinColumn(name = "caracteristica_id")
    )
    private List<Caracteristica> caracteristicas;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
