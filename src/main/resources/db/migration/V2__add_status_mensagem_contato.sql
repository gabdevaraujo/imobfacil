-- =============================================================================
-- V2__add_status_mensagem_contato.sql
-- Adiciona ciclo de vida ao lead (status + observacoes)
-- =============================================================================

ALTER TABLE mensagem_contato
    ADD COLUMN IF NOT EXISTS status      VARCHAR(30) NOT NULL DEFAULT 'NOVO',
    ADD COLUMN IF NOT EXISTS observacoes TEXT;

CREATE INDEX IF NOT EXISTS idx_contato_status ON mensagem_contato (status);
