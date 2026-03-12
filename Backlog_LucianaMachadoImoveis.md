# Levantamento de Requisitos e Backlog Backend - Luciana Machado Imóveis

Este documento detalha os requisitos funcionais, a estrutura de dados e o backlog de desenvolvimento para a criação de um sistema backend inspirado no site [lucianamachadoimoveis.com.br](https://lucianamachadoimoveis.com.br/).

---

## 1. Requisitos Funcionais do Sistema

- **Gestão de Catálogo:** CRUD completo de imóveis com suporte a tipos de negócio (Aluguel/Venda), categorias e status (Pronto, Em Construção, etc.).
- **Motor de Busca Performático:** Filtros avançados por localização (bairro, cidade), faixa de preço, número de quartos, vagas e área.
- **Galeria de Mídia:** Upload e ordenação de múltiplas imagens por imóvel.
- **Gestão de Leads:** Captura de mensagens de interesse vinculadas a imóveis ou contatos genéricos.
- **Administração de Corretores:** Cadastro de corretores responsáveis com CRECI e dados de contato direto.
- **Segurança:** Área administrativa protegida por autenticação JWT para gestão do inventário.

---

## 2. Estrutura de Dados Proposta (Relacional)

### Entidades Principais:
- **Imovel:** ID, Referência, Título, Descrição, Preço, Tipo Negócio, Área Total/Privativa, Quartos, Vagas, Suítes, Banheiros, Status.
- **Endereco:** Logradouro, Número, Complemento, Bairro, Cidade, Estado, CEP, Latitude/Longitude.
- **Imagem:** URL, Descrição, Ordem, ID_Imovel.
- **Caracteristica:** Nome (ex: "Piscina", "Ar Condicionado", "Portaria 24h").
- **Corretor:** Nome, CRECI, Telefone, Email, Foto.
- **Lead/Mensagem:** Nome, Email, Telefone, Mensagem, ID_Imovel (opcional), Data.

---

## 3. Backlog Técnico (User Stories & AI Prompts)

Abaixo estão as histórias de usuário formatadas como prompts para IAs generativas (Copilot, Gemini, ChatGPT).

### História 1: Configuração Base e Modelagem (Prisma/Node)
**Prompt:**
> "Atue como um desenvolvedor Backend Senior. Configure um projeto Node.js com TypeScript e Express. Utilize Prisma ORM com PostgreSQL. Implemente o esquema de banco de dados para um sistema imobiliário contendo as tabelas: `Imovel` (ref, titulo, descricao, preco, tipo_negocio, area, quartos, vagas, status), `Endereco` (logradouro, bairro, cidade, uf, cep, coordenadas), `Imagem` (url, ordem, id_imovel), `Caracteristica` (nome) e `Corretor` (nome, creci, telefone). Configure as relações: Imovel tem um Endereço, um Corretor, múltiplas Imagens e múltiplas Características (N:N). Gere os arquivos de migração e os modelos Prisma."

### História 2: API de Busca com Filtros Dinâmicos
**Prompt:**
> "Desenvolva um endpoint GET `/api/imoveis` que suporte filtros complexos. A lógica deve permitir filtrar por: `negocio` (Venda/Aluguel), `tipo` (Casa/Apartamento/Terreno), `minPreco`, `maxPreco`, `cidade`, `bairro` e `minQuartos`. Implemente paginação (page, limit) e ordenação por preço ou data de cadastro. O retorno deve incluir a primeira imagem (thumbnail) e os dados básicos de localização. Use o Prisma Client para construir a query dinamicamente baseada nos parâmetros da URL."

### História 3: Detalhes do Imóvel e Galeria
**Prompt:**
> "Crie o endpoint GET `/api/imoveis/:id` para retornar todos os detalhes de um imóvel. A resposta deve incluir o objeto completo do imóvel, o array de imagens ordenado pelo campo `ordem`, a lista de características e os dados de contato do corretor responsável. Implemente tratamento de erro para caso o ID não exista (404). Adicionalmente, crie um serviço de upload de imagens usando o middleware Multer, integrando com um provedor de storage (como S3 ou local), garantindo que cada imagem seja vinculada ao ID do imóvel."

### História 4: Captura de Leads e Notificação
**Prompt:**
> "Implemente um sistema de captura de leads. Crie uma tabela `MensagemContato` com campos: `nome`, `email`, `telefone`, `mensagem`, `id_imovel` (opcional) e `data_envio`. Desenvolva o endpoint POST `/api/contato`. Valide os campos usando a biblioteca Zod (email válido, telefone obrigatório). Ao receber uma mensagem, o sistema deve salvar no banco e disparar uma simulação de notificação (log ou serviço de email) para o corretor responsável, caso um imóvel esteja vinculado."

### História 5: Autenticação e Área Administrativa (JWT)
**Prompt:**
> "Implemente um sistema de autenticação JWT para a área administrativa. Crie uma tabela `Usuario` (admin). Desenvolva endpoints de `/api/auth/login` e um middleware de autenticação `ensureAuthenticated`. Proteja as rotas de escrita (POST, PUT, DELETE) de imóveis e corretores, permitindo que apenas usuários autenticados gerenciem o catálogo. Utilize `bcryptjs` para o hash de senhas e garanta que as senhas nunca sejam retornadas nas queries de usuário."

---

## 4. Próximos Passos Sugeridos

1. Inicializar o repositório Git.
2. Configurar o ambiente Docker para o banco de dados PostgreSQL.
3. Executar o **Prompt da História 1** para criar o esqueleto do projeto.
4. Validar as rotas básicas usando Insomnia ou Postman.
