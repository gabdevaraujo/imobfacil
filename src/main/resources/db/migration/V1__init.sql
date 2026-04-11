-- =============================================================================
-- V1__init.sql
-- Esquema inicial do Imobfácil — compatível com PostgreSQL
-- Gerado em: 2026-04-11
-- =============================================================================

CREATE TABLE IF NOT EXISTS usuario (
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha    VARCHAR(255) NOT NULL,
    email    VARCHAR(150),
    ativo    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS corretor (
    id       BIGSERIAL PRIMARY KEY,
    nome     VARCHAR(150) NOT NULL,
    creci    VARCHAR(50)  NOT NULL UNIQUE,
    telefone VARCHAR(30),
    email    VARCHAR(150) NOT NULL UNIQUE,
    foto     VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS caracteristica (
    id   BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS endereco (
    id          BIGSERIAL PRIMARY KEY,
    logradouro  VARCHAR(255) NOT NULL,
    numero      VARCHAR(20),
    complemento VARCHAR(100),
    bairro      VARCHAR(100) NOT NULL,
    cidade      VARCHAR(100) NOT NULL,
    estado      CHAR(2)      NOT NULL,
    cep         VARCHAR(10),
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS imovel (
    id                BIGSERIAL PRIMARY KEY,
    referencia        VARCHAR(50)    NOT NULL UNIQUE,
    titulo            VARCHAR(255)   NOT NULL,
    descricao         TEXT,
    preco             NUMERIC(15, 2) NOT NULL,
    tipo_negocio      VARCHAR(30)    NOT NULL,
    tipo_imovel       VARCHAR(30)    NOT NULL,
    area_total_m2     NUMERIC(10, 2) NOT NULL,
    area_privativa_m2 NUMERIC(10, 2),
    quartos           INTEGER        NOT NULL DEFAULT 0,
    suites            INTEGER                 DEFAULT 0,
    banheiros         INTEGER                 DEFAULT 0,
    vagas             INTEGER                 DEFAULT 0,
    status            VARCHAR(30)    NOT NULL DEFAULT 'DISPONIVEL',
    endereco_id       BIGINT UNIQUE REFERENCES endereco (id),
    corretor_id       BIGINT        REFERENCES corretor (id),
    data_criacao      TIMESTAMP NOT NULL DEFAULT NOW(),
    data_atualizacao  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS imagem (
    id        BIGSERIAL PRIMARY KEY,
    url       VARCHAR(500) NOT NULL,
    descricao VARCHAR(255),
    ordem     INTEGER      NOT NULL DEFAULT 0,
    imovel_id BIGINT       NOT NULL REFERENCES imovel (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS imovel_caracteristica (
    imovel_id        BIGINT NOT NULL REFERENCES imovel (id) ON DELETE CASCADE,
    caracteristica_id BIGINT NOT NULL REFERENCES caracteristica (id) ON DELETE CASCADE,
    PRIMARY KEY (imovel_id, caracteristica_id)
);

CREATE TABLE IF NOT EXISTS mensagem_contato (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    telefone   VARCHAR(30)  NOT NULL,
    mensagem   TEXT         NOT NULL,
    imovel_id  BIGINT       REFERENCES imovel (id) ON DELETE SET NULL,
    data_envio TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Índices de busca frequente
CREATE INDEX IF NOT EXISTS idx_imovel_tipo_negocio  ON imovel (tipo_negocio);
CREATE INDEX IF NOT EXISTS idx_imovel_tipo_imovel   ON imovel (tipo_imovel);
CREATE INDEX IF NOT EXISTS idx_imovel_status        ON imovel (status);
CREATE INDEX IF NOT EXISTS idx_imovel_preco         ON imovel (preco);
CREATE INDEX IF NOT EXISTS idx_imovel_corretor      ON imovel (corretor_id);
CREATE INDEX IF NOT EXISTS idx_endereco_cidade      ON endereco (cidade);
CREATE INDEX IF NOT EXISTS idx_endereco_bairro      ON endereco (bairro);
CREATE INDEX IF NOT EXISTS idx_imagem_imovel        ON imagem (imovel_id, ordem);
CREATE INDEX IF NOT EXISTS idx_contato_imovel       ON mensagem_contato (imovel_id);
