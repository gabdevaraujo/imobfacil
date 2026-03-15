# ImobFacil - Sistema de Gestão Imobiliária

## Visão Geral

Sistema backend para gestão de imóveis, estruturado em arquitetura MVC com Spring Boot 4.0.3 e Java 25.

## Estrutura do Projeto

```
br.com.gva.imobfacil/
├── model/              # Entidades JPA
│   ├── Imovel
│   ├── Endereco
│   ├── Imagem
│   ├── Corretor
│   ├── Caracteristica
│   ├── MensagemContato
│   ├── Usuario
│   ├── TipoNegocio (enum)
│   └── StatusImovel (enum)
├── repository/         # Spring Data JPA Repositories
├── service/            # Lógica de negócio
├── controller/         # REST Controllers
├── dto/               # Data Transfer Objects
└── config/            # Configurações (CORS, JWT, etc.)
```

## Tecnologias

- **Framework**: Spring Boot 4.0.3
- **Banco de Dados**: H2 (em desenvolvimento), PostgreSQL (produção)
- **ORM**: Hibernate + Spring Data JPA
- **Segurança**: Spring Security + JWT
- **Validation**: Jakarta Bean Validation
- **Lombock**: Para redução de boilerplate

## Endpoints da API

### Base URL: `/api`

### 1. Imóveis

#### Criar Imóvel
```http
POST /api/imoveis
Content-Type: application/json

{
  "referencia": "REF-001",
  "titulo": "Apartamento no Centro",
  "descricao": "Apartamento de 2 quartos...",
  "preco": 250000.00,
  "tipoNegocio": "VENDA",
  "areaTotalM2": 100.00,
  "areaPrivativaM2": 85.00,
  "quartos": 2,
  "suites": 1,
  "banheiros": 2,
  "vagas": 1,
  "status": "DISPONIVEL",
  "endereco": {...},
  "corretor": {...},
  "caracteristicas": [...]
}
```

#### Listar Imóveis (Com Paginação)
```http
GET /api/imoveis?page=0&size=10&sort=preco,desc
```

#### Buscar com Filtros
```http
GET /api/imoveis?minPreco=100000&maxPreco=500000&minQuartos=2&tipoNegocio=VENDA&cidade=Salvador&bairro=Barra
```

#### Obter Imóvel por ID
```http
GET /api/imoveis/{id}
```

#### Obter por Referência
```http
GET /api/imoveis/referencia/{referencia}
```

#### Buscar por Tipo de Negócio
```http
GET /api/imoveis/tipo/{tipoNegocio}
```

#### Buscar por Status
```http
GET /api/imoveis/status/{status}
```

#### Buscar por Cidade
```http
GET /api/imoveis/cidade/{cidade}
```

#### Buscar por Bairro
```http
GET /api/imoveis/bairro/{bairro}
```

#### Atualizar Imóvel
```http
PUT /api/imoveis/{id}
Content-Type: application/json
```

#### Deletar Imóvel
```http
DELETE /api/imoveis/{id}
```

### 2. Corretores

#### Criar Corretor
```http
POST /api/corretores
Content-Type: application/json

{
  "nome": "João Silva",
  "creci": "123456/SP",
  "telefone": "(11) 98765-4321",
  "email": "joao@imobfacil.com",
  "foto": "url-da-foto"
}
```

#### Listar Corretores
```http
GET /api/corretores?page=0&size=10
```

#### Obter Corretor por ID
```http
GET /api/corretores/{id}
```

#### Obter por Email
```http
GET /api/corretores/email/{email}
```

#### Obter por CRECI
```http
GET /api/corretores/creci/{creci}
```

#### Atualizar Corretor
```http
PUT /api/corretores/{id}
```

#### Deletar Corretor
```http
DELETE /api/corretores/{id}
```

### 3. Características

#### Criar Característica
```http
POST /api/caracteristicas
Content-Type: application/json

{
  "nome": "Piscina"
}
```

#### Listar Características
```http
GET /api/caracteristicas?page=0&size=20
```

#### Obter por ID
```http
GET /api/caracteristicas/{id}
```

#### Obter por Nome
```http
GET /api/caracteristicas/nome/{nome}
```

#### Atualizar Característica
```http
PUT /api/caracteristicas/{id}
```

#### Deletar Característica
```http
DELETE /api/caracteristicas/{id}
```

### 4. Mensagens de Contato

#### Enviar Mensagem
```http
POST /api/contato
Content-Type: application/json

{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "telefone": "(11) 99876-5432",
  "mensagem": "Gostaria de mais informações sobre o imóvel...",
  "imovelId": 1
}
```

#### Listar Mensagens
```http
GET /api/contato?page=0&size=10
```

#### Obter Mensagem por ID
```http
GET /api/contato/{id}
```

#### Listar Mensagens por Imóvel
```http
GET /api/contato/imovel/{imovelId}?page=0&size=10
```

#### Deletar Mensagem
```http
DELETE /api/contato/{id}
```

## Enums

### TipoNegocio
- `VENDA` - Venda de imóvel
- `ALUGUEL` - Aluguel permanente
- `ALUGUEL_TEMPORADA` - Aluguel temporário

### StatusImovel
- `DISPONIVEL` - Imóvel disponível para venda/aluguel
- `ALUGADO` - Imóvel alugado
- `VENDIDO` - Imóvel vendido
- `INDISPONIVEL` - Indisponível temporariamente
- `EM_CONSTRUCAO` - Em fase de construção

## Configuração

### Banco de Dados (application.yaml)
- **Desenvolvimento**: H2 em memória (jdbc:h2:mem:imobfacildb)
- **Produção**: Configure PostgreSQL

### Console H2
- URL: `http://localhost:8080/api/h2-console`
- Acesso livre em desenvolvimento

## Como Rodar

### Pré-requisitos
- Java 25+
- Maven 3.6+

### Compilar e Rodar
```bash
cd /home/gabriel/Dev/imobfacil
mvn clean package
mvn spring-boot:run
```

### Testes
```bash
mvn test
```

## Próximas Fases

1. **Autenticação JWT**: Implementar segurança com tokens JWT
2. **Upload de Imagens**: Integrar multer e storage (S3 ou local)
3. **Notificações de Email**: Enviar emails para corretores
4. **Validação Avançada**: Validação de CPF, CRECI, CEP
5. **Relatórios**: Gerar relatórios de vendas/aluguéis
6. **Dashboard**: Criar dashboard analítico

## DTOs Disponíveis

- `ImovelDTO` - Transfer de dados de imóvel
- `EnderecoDTO` - Transfer de dados de endereço
- `CorretorDTO` - Transfer de dados de corretor
- `ImagemDTO` - Transfer de dados de imagem
- `CaracteristicaDTO` - Transfer de dados de característica
- `MensagemContatoDTO` - Transfer de dados de mensagem

## Autores

Gabriel V. Andrade

## Licença

Proprietary - Luciana Machado Imóveis
