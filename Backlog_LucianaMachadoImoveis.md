# Levantamento de Requisitos e Backlog Backend - Luciana Machado Imóveis

Este documento detalha os requisitos funcionais, a estrutura de dados e o backlog de desenvolvimento para a criação de um sistema backend inspirado no site [lucianamachadoimoveis.com.br](https://lucianamachadoimoveis.com.br/).

---

## 1. Requisitos Funcionais do Sistema

- **Gestão de Catálogo:** CRUD completo de imóveis com suporte a tipos de negócio (Aluguel/Venda/Temporada), tipo de imóvel (Apartamento, Casa, Cobertura, Studio, Terreno, Comercial) e status (Disponível, Alugado, Vendido, Em Construção, Indisponível).
- **Motor de Busca Performático:** Filtros avançados por localização (bairro, cidade), tipo de negócio, tipo de imóvel, faixa de preço, número de quartos, vagas e área. Paginação e ordenação em todas as listagens.
- **Galeria de Mídia:** Upload, remoção e reordenação de múltiplas imagens por imóvel. Thumbnail automático (primeira imagem pela ordem).
- **Gestão de Leads:** Captura de mensagens de interesse vinculadas a imóveis ou contatos genéricos, com ciclo de vida do lead (Novo → Em Contato → Fechado / Descartado).
- **Administração de Corretores:** Cadastro de corretores responsáveis com CRECI e dados de contato direto.
- **Segurança:** Área administrativa protegida por autenticação JWT. Rotas de escrita exigem token válido.
- **Observabilidade e Qualidade:** Documentação interativa da API (OpenAPI 3), testes automatizados cobrindo camada de serviço e endpoints REST, e migrações de banco versionadas para ambientes de produção.
- **Notificações:** Envio de e-mail ao corretor responsável ao receber um novo lead vinculado ao seu imóvel.

---

## 2. Estrutura de Dados (Relacional)

### Entidades implementadas:
- **Imovel:** ID, Referência, Título, Descrição, Preço, TipoNegocio, TipoImovel, Área Total/Privativa, Quartos, Suítes, Banheiros, Vagas, Status, DataCriacao, DataAtualizacao.
- **Endereco:** Logradouro, Número, Complemento, Bairro, Cidade, Estado, CEP, Latitude, Longitude.
- **Imagem:** URL, Descrição, Ordem, ID_Imovel.
- **Caracteristica:** Nome (ex: "Piscina", "Ar Condicionado", "Portaria 24h").
- **Corretor:** Nome, CRECI, Telefone, Email, Foto.
- **MensagemContato:** Nome, Email, Telefone, Mensagem, ID_Imovel (opcional), DataEnvio.
- **Usuario:** Username, Senha (BCrypt), Email, Ativo.

### Evoluções de dados previstas (novas histórias):
- **MensagemContato:** campo `status` (NOVO, EM_CONTATO, FECHADO, DESCARTADO) e `observacoes` (texto livre).
- **Flyway:** migração inicial `V1__init.sql` consolidando o esquema atual para rastreamento versionado em produção.

---

## 3. Backlog Técnico (User Stories & AI Prompts)

> **Legenda de status:** ✅ Implementado | 🔲 Pendente

---

### ✅ História 1: Configuração Base e Modelagem

**Objetivo:** Estruturar o projeto Spring Boot com JPA/Hibernate, H2 (dev) e PostgreSQL (prod), modelando todas as entidades e suas relações.

**Critérios de aceite:**
- Entidades `Imovel`, `Endereco`, `Imagem`, `Caracteristica`, `Corretor`, `MensagemContato` e `Usuario` persistidas com DDL gerado pelo Hibernate.
- Relações mapeadas: `Imovel` 1:1 `Endereco`, N:1 `Corretor`, 1:N `Imagem`, N:N `Caracteristica`, 1:N `MensagemContato`.
- Profiles `default` (H2, create-drop) e `prod` (PostgreSQL, validate) funcionando.
- Docker Compose com `postgres:16-alpine` e serviço da aplicação com health check.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Configure um projeto Spring Boot 4 com Spring Data JPA e H2 para desenvolvimento. Implemente as entidades JPA para um sistema imobiliário: `Imovel` (referencia, titulo, descricao, preco, tipoNegocio, tipoImovel, areaTotalM2, areaPrivativaM2, quartos, suites, banheiros, vagas, status), `Endereco` (logradouro, numero, complemento, bairro, cidade, estado, cep, latitude, longitude), `Imagem` (url, descricao, ordem), `Caracteristica` (nome) e `Corretor` (nome, creci, telefone, email, foto). Configure as relações via anotações JPA. Crie profiles `default` com H2 e `prod` com PostgreSQL. Adicione Docker Compose com banco de dados e healthcheck."

---

### ✅ História 2: API de Busca com Filtros Dinâmicos

**Objetivo:** Endpoint de listagem com filtros opcionais combinados, paginação e retorno resumido com thumbnail.

**Critérios de aceite:**
- `GET /imoveis` aceita os parâmetros: `tipoNegocio`, `tipoImovel`, `status`, `cidade`, `bairro`, `minPreco`, `maxPreco`, `minQuartos`.
- Resposta paginada (`Page<ImovelResumoDTO>`) com: id, referencia, titulo, preco, tipoNegocio, tipoImovel, quartos, vagas, areaTotalM2, status, thumbnail (URL da imagem de ordem 1), cidade, bairro, estado.
- Parâmetros ausentes não restringem a busca (todos os filtros são opcionais).
- Ordenação por `dataCriacao,desc` como padrão.

**Prompt:**
> "Desenvolva um endpoint `GET /imoveis` em Spring Boot que suporte filtros dinâmicos via JPQL com parâmetros opcionais: `tipoNegocio`, `tipoImovel`, `status`, `cidade`, `bairro`, `minPreco`, `maxPreco` e `minQuartos`. Implemente paginação com `Pageable`. O retorno deve ser `Page<ImovelResumoDTO>` contendo apenas os dados necessários para o card de listagem, incluindo a URL da imagem de menor ordem (thumbnail). Use `@Query` com `:param IS NULL OR campo = :param` para filtros opcionais."

---

### ✅ História 3: Detalhes do Imóvel e Galeria de Imagens

**Objetivo:** Endpoint de detalhe completo e endpoints para upload e remoção de imagens.

**Critérios de aceite:**
- `GET /imoveis/{id}` retorna `ImovelDTO` completo com imagens ordenadas por `ordem`, lista de características e dados do corretor.
- `GET /imoveis/referencia/{ref}` retorna o mesmo detalhe por código de referência.
- `POST /imoveis/{id}/imagens` aceita `multipart/form-data` com `arquivo` (imagem) e `descricao` opcional. Salva o arquivo em disco e persiste a `Imagem` com a próxima ordem disponível.
- `DELETE /imoveis/{id}/imagens/{imagemId}` remove o arquivo físico e reordena as imagens restantes a partir de 1.
- `404` com `ProblemDetail` para IDs inexistentes.

**Prompt:**
> "Crie o endpoint `GET /imoveis/{id}` retornando o imóvel completo com imagens ordenadas por `ordem`, lista de características e corretor. Implemente `404` via `@RestControllerAdvice` com `ProblemDetail`. Adicione `POST /imoveis/{id}/imagens` usando `MultipartFile` para upload de imagens salvas em diretório configurável (`app.upload.dir`), atribuindo automaticamente a próxima ordem. Adicione `DELETE /imoveis/{id}/imagens/{imagemId}` que remove o arquivo físico e reordena as demais imagens sequencialmente."

---

### ✅ História 4: Captura de Leads

**Objetivo:** Endpoint público para receber mensagens de interesse, com validação e notificação via log ao corretor.

**Critérios de aceite:**
- `POST /contato` é público (sem autenticação).
- Valida `nome`, `email` (formato), `telefone` e `mensagem` como obrigatórios; `imovelId` é opcional.
- Salva `MensagemContato` no banco.
- Emite log estruturado simulando notificação ao corretor responsável quando um imóvel está vinculado.
- Endpoints administrativos (`GET /contato`, `GET /contato/{id}`, `GET /contato/imovel/{id}`, `DELETE /contato/{id}`) protegidos por JWT.

**Prompt:**
> "Implemente o sistema de captura de leads. Crie `MensagemContato` com campos: `nome`, `email`, `telefone`, `mensagem`, `imovelId` (opcional) e `dataEnvio`. Desenvolva `POST /contato` com Bean Validation (`@NotBlank`, `@Email`). Ao salvar, emita log estruturado simulando notificação ao corretor. Proteja com Spring Security os endpoints de leitura e deleção, deixando apenas o POST público."

---

### ✅ História 5: Autenticação e Área Administrativa (JWT)

**Objetivo:** Autenticação stateless via JWT protegendo todas as rotas de escrita.

**Critérios de aceite:**
- `POST /auth/login` recebe `username` e `senha`, retorna `{ token, expiresIn }`.
- Token JWT assinado com chave HMAC-SHA256 configurada via variável de ambiente (`JWT_SECRET`).
- `JwtAuthenticationFilter` valida o token em cada requisição e popula o `SecurityContext`.
- Rotas `GET` de imóveis, corretores e características são públicas; todas as escritas exigem token.
- Senhas armazenadas com BCrypt; campo `senha` nunca retornado em responses.

**Prompt:**
> "Implemente autenticação JWT em Spring Boot 4 com Spring Security 7. Crie `POST /auth/login` que autentica o usuário via `DaoAuthenticationProvider` e retorna um JWT assinado com HMAC-SHA256. Implemente `OncePerRequestFilter` para validar o token. Configure `SecurityFilterChain` stateless: rotas GET de catálogo são públicas, rotas de escrita exigem `Bearer` token. Use `BCryptPasswordEncoder` para senhas e externalize `JWT_SECRET` via variável de ambiente."

---

### 🔲 História 6: Documentação Interativa da API (OpenAPI 3 / Swagger)

**Prioridade:** Alta — impacto direto na adoção e integração por front-ends e parceiros.

**Objetivo:** Gerar e expor documentação completa e interativa de todos os endpoints da API, incluindo exemplos de request/response e autenticação por token.

**Contexto:**
Atualmente a API é documentada apenas por uma collection Postman mantida manualmente. Qualquer novo endpoint ou alteração de contrato exige atualização manual. Uma documentação gerada a partir do código garante que estará sempre sincronizada e pode ser acessada diretamente no ambiente de desenvolvimento.

**Critérios de aceite:**
- Swagger UI disponível em `/swagger-ui.html` no perfil `default` (dev). Desabilitado em `prod`.
- Todos os controllers anotados com `@Operation`, `@ApiResponse` e `@Schema` nos DTOs.
- Autenticação via `Bearer Token` configurada no Swagger UI (botão "Authorize").
- Endpoints agrupados por tag: `Autenticação`, `Imóveis`, `Imagens`, `Corretores`, `Características`, `Contato`.
- Enums `TipoNegocio`, `TipoImovel` e `StatusImovel` com descrição de cada valor.
- Respostas de erro documentadas (`400`, `401`, `403`, `404`, `500`) com o formato `ProblemDetail`.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Integre o SpringDoc OpenAPI 3 (`springdoc-openapi-starter-webmvc-ui`) ao projeto Spring Boot 4. Configure a dependência no `pom.xml`. Desabilite o Swagger UI no perfil `prod` via `springdoc.swagger-ui.enabled=false`. Anote todos os controllers com `@Tag`, `@Operation` e `@ApiResponse`. Adicione `@Schema` nos DTOs de request e response. Configure suporte a `Bearer Token` no `OpenApiConfig` via `@SecurityScheme`. Agrupe os endpoints pelas tags: Autenticação, Imóveis, Imagens, Corretores, Características e Contato. Documente os valores possíveis dos enums e o formato `ProblemDetail` nas respostas de erro."

---

### 🔲 História 7: Testes Automatizados (Camada de Serviço e Controllers)

**Prioridade:** Alta — pré-requisito para deploy contínuo seguro e para evolução sem regressões.

**Objetivo:** Cobrir com testes automatizados os fluxos críticos do sistema: serviços de negócio (unitários com Mockito) e endpoints REST (integração com `@WebMvcTest`).

**Contexto:**
O sistema opera sem nenhum teste automatizado. Qualquer alteração em `ImovelService`, `AuthService` ou nos controllers é validada apenas manualmente via Postman. Isso torna cada deploy um risco e inviabiliza a adição de CI/CD com gates de qualidade.

**Critérios de aceite:**
- Cobertura mínima de 80% nas classes de serviço (`ImovelService`, `CorretorService`, `MensagemContatoService`, `AuthService`, `ImagemUploadService`).
- `ImovelServiceTest`: cenários de busca com e sem filtros, criação com corretor inexistente (deve lançar `RecursoNaoEncontradoException`), remoção de imóvel com imagens associadas.
- `ImovelControllerTest` com `@WebMvcTest`: `GET /imoveis` retorna 200 com página, `POST /imoveis` sem token retorna 401, `POST /imoveis` com body inválido retorna 400 com mapa de erros.
- `AuthServiceTest`: login com credenciais válidas retorna token, login com senha errada lança exceção.
- `ImagemUploadServiceTest`: upload salva arquivo e persiste entidade, delete remove arquivo e reordena restantes.
- Nenhum teste usa `@SpringBootTest` onde `@WebMvcTest` ou `@ExtendWith(MockitoExtension.class)` são suficientes.
- Nomenclatura: `should[Acao]When[Condicao]`.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Escreva testes automatizados para o projeto Spring Boot 4 seguindo a pirâmide de testes. Use JUnit 5, Mockito e AssertJ. Para a camada de serviço, use `@ExtendWith(MockitoExtension.class)` com mocks de repositórios; cubra fluxos felizes, not-found e validação. Para controllers, use `@WebMvcTest` com `@MockBean` nos serviços; valide status HTTP, corpo JSON e autenticação. Nomeie os métodos de teste como `should[Acao]When[Condicao]`. Implemente testes para `ImovelService`, `AuthService`, `ImagemUploadService` e seus respectivos controllers. Garanta ao menos 80% de cobertura nas classes de serviço."

---

### 🔲 História 8: Ciclo de Vida dos Leads (Status e Observações)

**Prioridade:** Média — valor direto para o time comercial da imobiliária acompanhar follow-ups.

**Objetivo:** Adicionar ao lead (MensagemContato) um campo de status de acompanhamento e um campo de observações, permitindo ao corretor ou administrador registrar o andamento de cada contato recebido.

**Contexto:**
Atualmente a `MensagemContato` é apenas um registro de entrada sem ciclo de vida. O time comercial não consegue distinguir quais leads já foram contactados, quais resultaram em visita e quais foram descartados. Isso força o controle em planilhas externas, gerando perda de informação.

**Critérios de aceite:**
- Campo `status` adicionado à entidade `MensagemContato` com valores: `NOVO`, `EM_CONTATO`, `VISITA_AGENDADA`, `FECHADO`, `DESCARTADO`. Valor padrão: `NOVO`.
- Campo `observacoes` (texto livre, opcional) para o time registrar anotações de acompanhamento.
- Novo endpoint `PATCH /contato/{id}/status` (autenticado) aceita `{ "status": "EM_CONTATO", "observacoes": "Retornou ligação, visita prevista para sexta." }`.
- `GET /contato` aceita filtro opcional `?status=NOVO` para listar leads por etapa.
- Response de `GET /contato/{id}` inclui o `status` e `observacoes` atuais.
- Migration Flyway `V2__add_status_mensagem_contato.sql` adiciona as colunas sem breaking change.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Evolua a entidade `MensagemContato` adicionando os campos `status` (enum: NOVO, EM_CONTATO, VISITA_AGENDADA, FECHADO, DESCARTADO; default NOVO) e `observacoes` (String, nullable). Crie o DTO `AtualizarStatusLeadRequest` com `@NotNull StatusLead status` e `observacoes` opcional. Implemente `PATCH /contato/{id}/status` no `MensagemContatoController` (autenticado), delegando ao `MensagemContatoService`. Adicione filtro opcional `?status=` ao `GET /contato`. Crie a migration Flyway `V2__add_status_mensagem_contato.sql` adicionando as colunas com `DEFAULT 'NOVO'` e `NULL` para observacoes."

---

### 🔲 História 9: Notificação Real por E-mail ao Corretor

**Prioridade:** Média — elimina a necessidade de o corretor verificar manualmente o painel para saber de novos leads.

**Objetivo:** Ao receber um novo lead via `POST /contato`, enviar um e-mail real ao corretor responsável pelo imóvel (quando vinculado) e ao endereço administrativo da imobiliária.

**Contexto:**
A notificação atual é apenas um `log.info`. Na prática, o corretor só descobre novos leads se acessar o painel administrativo. Um e-mail automático reduz o tempo de resposta ao cliente, fator crítico em negociações imobiliárias.

**Critérios de aceite:**
- Envio de e-mail disparado de forma assíncrona (`@Async`) para não bloquear o `POST /contato`.
- Template de e-mail em HTML (`Thymeleaf`) contendo: nome do lead, telefone, e-mail, mensagem, link para o imóvel (referência) e nome do corretor destinatário.
- Configuração via variáveis de ambiente: `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM`, `MAIL_ADMIN`.
- Se nenhum imóvel estiver vinculado, o e-mail é enviado apenas para `MAIL_ADMIN`.
- Falha no envio de e-mail **não** deve impedir o salvamento do lead (tratamento de exceção isolado com log de erro).
- Perfil `default` (dev) usa `spring.mail.host=localhost` com Mailhog ou `mailtrap.io` para testes sem envio real.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Implemente envio de e-mail assíncrono no Spring Boot 4 usando `spring-boot-starter-mail` e `Thymeleaf` para templates HTML. Crie `EmailService` com método `@Async enviarNotificacaoLead(MensagemContato lead)`. O template `notificacao-lead.html` deve exibir os dados do lead e do imóvel. Configure `JavaMailSender` via propriedades externalizadas (`MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`). No `MensagemContatoService`, chame o `EmailService` após salvar o lead. Envolva o envio em try-catch para que uma falha de e-mail não faça o endpoint retornar erro. Habilite `@EnableAsync` na aplicação."

---

### 🔲 História 10: Migrações Versionadas com Flyway

**Prioridade:** Média — necessária para operar com segurança em produção e suportar as evoluções de schema das histórias 8 e futuras.

**Objetivo:** Substituir o `ddl-auto: validate` por um esquema gerenciado via Flyway, com scripts SQL versionados que garantem rastreabilidade e reversibilidade das mudanças de banco em produção.

**Contexto:**
O ambiente de produção usa `ddl-auto: validate`, o que significa que qualquer alteração de schema (ex.: adicionar a coluna `status` da História 8) exige intervenção manual no banco. Sem Flyway, não há histórico de quando cada coluna foi adicionada, por quem ou por qual release. Isso é um risco operacional crescente conforme o schema evolui.

**Critérios de aceite:**
- Flyway configurado como dependência no `pom.xml`.
- `V1__init.sql`: script que cria do zero todas as tabelas do esquema atual (usuário, corretor, característica, endereço, imóvel, imagem, mensagem_contato e tabela N:N), compatível com PostgreSQL.
- No perfil `default` (H2/dev), Flyway habilitado com `spring.flyway.enabled=true` e `locations=classpath:db/migration`. O `data.sql` continua sendo carregado via `sql.init.mode=always` após as migrations.
- No perfil `prod`, `ddl-auto: none` e Flyway gerencia o schema completo.
- `application-prod.yaml` atualizado para remover `ddl-auto: validate` e apontar para `classpath:db/migration`.
- Migrations futuras seguem o padrão `V{n}__{descricao_snake_case}.sql`.

**Prompt:**
> "Atue como um desenvolvedor Backend Senior Java. Integre o Flyway ao projeto Spring Boot 4. Adicione `flyway-core` ao `pom.xml`. Crie `src/main/resources/db/migration/V1__init.sql` com o DDL completo do esquema atual (tabelas: usuario, corretor, caracteristica, endereco, imovel, imagem, imovel_caracteristica, mensagem_contato), compatível com PostgreSQL e H2. Configure o perfil `default` para rodar Flyway antes de carregar o `data.sql`. Configure o perfil `prod` com `spring.jpa.hibernate.ddl-auto=none` e `spring.flyway.enabled=true`. Garanta que novas migrations sigam o padrão `V{n}__{descricao}.sql`."

---

## 4. Roadmap e Priorização

| # | História | Prioridade | Dependências | Esforço estimado |
|---|---|---|---|---|
| 1–5 | Configuração, Busca, Galeria, Leads, JWT | — | ✅ Implementadas | — |
| 6 | Documentação OpenAPI (Swagger) | 🔴 Alta | Nenhuma | Pequeno |
| 7 | Testes Automatizados | 🔴 Alta | Nenhuma | Grande |
| 10 | Migrações com Flyway | 🟡 Média | Nenhuma | Médio |
| 8 | Ciclo de Vida dos Leads | 🟡 Média | H10 (Flyway) | Médio |
| 9 | Notificação Real por E-mail | 🟡 Média | H8 | Médio |

**Ordem de execução recomendada:** H6 → H7 → H10 → H8 → H9

---

## 5. Próximos Passos

1. Implementar **H6 (Swagger)** — baixo esforço, alto impacto para testes de integração com front-end.
2. Implementar **H7 (Testes)** — pré-requisito para adotar CI/CD com gates de qualidade.
3. Implementar **H10 (Flyway)** — desbloqueia a adição segura de colunas em produção.
4. Implementar **H8 (Status de Leads)** — após Flyway garantir que a migration `V2` seja aplicada corretamente.
5. Implementar **H9 (E-mail)** — após ciclo de vida do lead estar estável.
