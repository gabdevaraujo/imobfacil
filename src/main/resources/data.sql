-- =============================================================
-- MASSA DE DADOS — Imobfácil (Luciana Machado Imóveis)
-- Carregado automaticamente pelo H2 a cada inicialização.
--
-- Senhas em BCrypt:
--   admin123 → $2a$10$d5RCrpqNCDDre89HpmOlheOS.VrMjxGbuUDBchXUjKJzotIVbq106
--   senha123 → $2a$10$qoPpIZ4jMPlPc7o4Rs8/H.yqDKi3gWFw6v27SESSqnK4szHYdaYY.
-- =============================================================


-- -------------------------------------------------------------
-- USUÁRIOS (área administrativa)
-- -------------------------------------------------------------
INSERT INTO usuario (id, username, senha, email, ativo) VALUES
(1, 'admin',      '$2a$10$d5RCrpqNCDDre89HpmOlheOS.VrMjxGbuUDBchXUjKJzotIVbq106', 'admin@imobfacil.com.br',      TRUE),
(2, 'joao.silva', '$2a$10$qoPpIZ4jMPlPc7o4Rs8/H.yqDKi3gWFw6v27SESSqnK4szHYdaYY.', 'joao.silva@imobfacil.com.br', TRUE);


-- -------------------------------------------------------------
-- CORRETORES
-- -------------------------------------------------------------
INSERT INTO corretor (id, nome, creci, telefone, email, foto) VALUES
(1, 'João Silva',     '123456-F', '(11) 99999-0001', 'joao.silva@imobfacil.com.br',     'https://randomuser.me/api/portraits/men/32.jpg'),
(2, 'Ana Costa',      '789012-F', '(11) 99999-0002', 'ana.costa@imobfacil.com.br',      'https://randomuser.me/api/portraits/women/44.jpg'),
(3, 'Roberto Mendes', '345678-F', '(21) 99999-0003', 'roberto.mendes@imobfacil.com.br', 'https://randomuser.me/api/portraits/men/65.jpg');


-- -------------------------------------------------------------
-- CARACTERÍSTICAS
-- -------------------------------------------------------------
INSERT INTO caracteristica (id, nome) VALUES
(1,  'Piscina'),
(2,  'Churrasqueira'),
(3,  'Academia'),
(4,  'Portaria 24h'),
(5,  'Ar Condicionado'),
(6,  'Sacada/Varanda'),
(7,  'Quadra Esportiva'),
(8,  'Salão de Festas'),
(9,  'Elevador'),
(10, 'Mobiliado');


-- -------------------------------------------------------------
-- ENDEREÇOS
-- -------------------------------------------------------------
INSERT INTO endereco (id, logradouro, numero, complemento, bairro, cidade, estado, cep, latitude, longitude) VALUES
(1, 'Rua das Flores',         '100',  'Apto 51',  'Jardins',       'São Paulo',     'SP', '01402-000', -23.5613, -46.6567),
(2, 'Avenida Paulista',       '1500', 'Cobertura', 'Bela Vista',   'São Paulo',     'SP', '01310-100', -23.5630, -46.6543),
(3, 'Rua da Consolação',      '200',  'Apto 12',  'Consolação',   'São Paulo',     'SP', '01301-000', -23.5558, -46.6490),
(4, 'Rua dos Pinheiros',      '350',  NULL,        'Pinheiros',    'São Paulo',     'SP', '05422-000', -23.5671, -46.6883),
(5, 'Rua da Glória',          '500',  'Apto 301', 'Glória',        'Rio de Janeiro','RJ', '20241-180', -22.9175, -43.1800),
(6, 'Avenida Atlântica',      '1000', 'Apto 802', 'Copacabana',   'Rio de Janeiro','RJ', '22010-000', -22.9691, -43.1823),
(7, 'Rua das Laranjeiras',    '750',  NULL,        'Laranjeiras',  'Rio de Janeiro','RJ', '22240-005', -22.9413, -43.1878),
(8, 'Rua XV de Novembro',     '250',  'Apto 42',  'Centro',        'Curitiba',      'PR', '80020-310', -25.4284, -49.2733);


-- -------------------------------------------------------------
-- IMÓVEIS
-- -------------------------------------------------------------
INSERT INTO imovel (id, referencia, titulo, descricao, preco, tipo_negocio, area_total_m2, area_privativa_m2, quartos, suites, banheiros, vagas, status, endereco_id, corretor_id, data_criacao, data_atualizacao) VALUES

(1, 'AP-001',
 'Apartamento Sofisticado nos Jardins',
 'Lindo apartamento com acabamento de alto padrão, piso de mármore, armários planejados em todos os cômodos e vista privilegiada. Prédio com portaria 24h e ampla área de lazer.',
 850000.00, 'VENDA', 110.0, 95.0, 3, 1, 2, 2, 'DISPONIVEL', 1, 1,
 '2025-11-10 09:00:00', '2025-11-10 09:00:00'),

(2, 'CB-001',
 'Cobertura Dúplex na Avenida Paulista',
 'Cobertura com terraço privativo, piscina própria e vista panorâmica para a cidade. Acabamento premium, home theater, sala de jantar para 12 pessoas e 3 vagas de garagem cobertas.',
 2500000.00, 'VENDA', 280.0, 250.0, 4, 3, 4, 3, 'DISPONIVEL', 2, 2,
 '2025-10-05 14:30:00', '2025-10-05 14:30:00'),

(3, 'AP-002',
 'Studio Moderno — Consolação',
 'Studio compacto e bem resolvido, totalmente mobiliado. Ideal para profissionais ou investidores. Próximo ao metrô Consolação, restaurantes e serviços.',
 3500.00, 'ALUGUEL', 38.0, 35.0, 1, 0, 1, 1, 'DISPONIVEL', 3, 1,
 '2026-01-08 10:00:00', '2026-01-08 10:00:00'),

(4, 'CA-001',
 'Casa Espaçosa em Pinheiros',
 'Casa com excelente aproveitamento de espaço, jardim privativo, 2 suítes, sala de estar ampla e cozinha integrada. Bairro nobre com fácil acesso à Paulista e ao Parque do Ibirapuera.',
 1800000.00, 'VENDA', 320.0, 280.0, 4, 2, 3, 3, 'DISPONIVEL', 4, 2,
 '2025-12-01 11:00:00', '2025-12-01 11:00:00'),

(5, 'AP-003',
 'Apartamento com Vista para o Mirante — Glória',
 'Apartamento bem localizado no bairro da Glória, Rio de Janeiro, com vista parcial para o Morro da Glória. Sala ampla, cozinha reformada e dois quartos com armários embutidos.',
 4200.00, 'ALUGUEL', 75.0, 68.0, 2, 1, 2, 1, 'DISPONIVEL', 5, 3,
 '2026-01-20 08:30:00', '2026-01-20 08:30:00'),

(6, 'AP-004',
 'Flat em Copacabana — Temporada',
 'Flat completamente equipado e decorado a 200m da praia de Copacabana. Perfeito para temporada, férias ou curtas estadias. Wi-Fi, TV a cabo e serviço de limpeza inclusos.',
 350.00, 'ALUGUEL_TEMPORADA', 55.0, 50.0, 1, 1, 1, 1, 'DISPONIVEL', 6, 3,
 '2026-02-01 15:00:00', '2026-02-01 15:00:00'),

(7, 'CA-002',
 'Mansão nas Laranjeiras',
 'Imóvel com 5 quartos, piscina, quadra de tênis, área gourmet e jardim paisagístico. Projeto arquitetônico assinado. Já vendido — disponível apenas para consulta histórica.',
 3200000.00, 'VENDA', 650.0, 580.0, 5, 3, 5, 4, 'VENDIDO', 7, 3,
 '2025-09-01 09:00:00', '2025-09-01 09:00:00'),

(8, 'AP-005',
 'Apartamento no Centro de Curitiba',
 'Apartamento reformado em localização central em Curitiba. Próximo à Rua XV de Novembro, Ópera de Arame e transporte público. Ótima opção para quem trabalha no centro.',
 2800.00, 'ALUGUEL', 65.0, 60.0, 2, 0, 1, 1, 'DISPONIVEL', 8, 1,
 '2026-02-15 12:00:00', '2026-02-15 12:00:00');


-- -------------------------------------------------------------
-- IMÓVEL × CARACTERÍSTICA (N:N)
-- -------------------------------------------------------------
INSERT INTO imovel_caracteristica (imovel_id, caracteristica_id) VALUES
-- AP-001 (Jardins): Portaria 24h, Ar Cond., Sacada, Elevador
(1, 4), (1, 5), (1, 6), (1, 9),
-- CB-001 (Paulista): Piscina, Churrasqueira, Academia, Portaria 24h, Quadra, Salão, Elevador
(2, 1), (2, 2), (2, 3), (2, 4), (2, 7), (2, 8), (2, 9),
-- AP-002 (Studio): Ar Cond., Sacada, Elevador, Mobiliado
(3, 5), (3, 6), (3, 9), (3, 10),
-- CA-001 (Pinheiros): Piscina, Churrasqueira, Portaria 24h, Sacada
(4, 1), (4, 2), (4, 4), (4, 6),
-- AP-003 (Glória): Ar Cond., Sacada, Elevador
(5, 5), (5, 6), (5, 9),
-- AP-004 (Copacabana): Ar Cond., Elevador, Mobiliado
(6, 5), (6, 9), (6, 10),
-- CA-002 (Laranjeiras): Piscina, Churrasqueira, Academia, Portaria 24h, Quadra, Salão
(7, 1), (7, 2), (7, 3), (7, 4), (7, 7), (7, 8),
-- AP-005 (Curitiba): Ar Cond., Elevador
(8, 5), (8, 9);


-- -------------------------------------------------------------
-- IMAGENS
-- -------------------------------------------------------------
INSERT INTO imagem (id, url, descricao, ordem, imovel_id) VALUES
-- AP-001
(1,  'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800', 'Sala de estar',    1, 1),
(2,  'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800', 'Cozinha',          2, 1),
(3,  'https://images.unsplash.com/photo-1540518614846-7eded433c457?w=800', 'Quarto principal', 3, 1),
-- CB-001
(4,  'https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800', 'Terraço',          1, 2),
(5,  'https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=800', 'Piscina privativa',2, 2),
(6,  'https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=800', 'Sala de jantar',   3, 2),
(7,  'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800', 'Suíte master',     4, 2),
-- AP-002 (Studio)
(8,  'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800', 'Ambiente integrado',1, 3),
(9,  'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=800', 'Banheiro',          2, 3),
-- CA-001 (Pinheiros)
(10, 'https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800', 'Fachada',          1, 4),
(11, 'https://images.unsplash.com/photo-1556020685-ae41abfc9365?w=800', 'Jardim',            2, 4),
(12, 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800', 'Sala de estar',    3, 4),
-- AP-003 (Glória)
(13, 'https://images.unsplash.com/photo-1555636222-cae831e670b3?w=800', 'Vista externa',    1, 5),
(14, 'https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800', 'Cozinha',         2, 5),
-- AP-004 (Copacabana)
(15, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800', 'Vista para o mar', 1, 6),
(16, 'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800', 'Quarto',           2, 6),
-- CA-002 (Laranjeiras)
(17, 'https://images.unsplash.com/photo-1571939228382-b2f2b585ce15?w=800', 'Fachada',          1, 7),
(18, 'https://images.unsplash.com/photo-1575429198097-0414ec08e8cd?w=800', 'Piscina',          2, 7),
(19, 'https://images.unsplash.com/photo-1596422846543-75c6fc197f07?w=800', 'Área gourmet',     3, 7),
-- AP-005 (Curitiba)
(20, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800', 'Sala',             1, 8),
(21, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800', 'Cozinha',           2, 8);


-- -------------------------------------------------------------
-- MENSAGENS DE CONTATO (leads)
-- -------------------------------------------------------------
INSERT INTO mensagem_contato (id, nome, email, telefone, mensagem, imovel_id, data_envio) VALUES

(1, 'Maria Oliveira',
 'maria.oliveira@email.com', '(11) 98888-0001',
 'Tenho interesse no apartamento dos Jardins. Qual a disponibilidade para visita esta semana?',
 1, '2026-03-10 14:22:00'),

(2, 'Carlos Pereira',
 'carlos.pereira@email.com', '(21) 97777-0002',
 'Gostaria de agendar uma visita à cobertura. Aceita permuta com apartamento em Moema?',
 2, '2026-03-12 09:15:00'),

(3, 'Fernanda Santos',
 'fernanda.santos@email.com', '(11) 96666-0003',
 'Vi o flat de Copacabana e tenho interesse para o mês de julho. Qual o valor mínimo de diárias?',
 6, '2026-03-15 17:45:00'),

(4, 'Ricardo Almeida',
 'ricardo.almeida@email.com', '(41) 95555-0004',
 'Preciso de um apartamento de 2 quartos para alugar em Curitiba, de preferência no centro. Vocês têm outras opções além do AP-005?',
 NULL, '2026-03-18 11:00:00');


-- -------------------------------------------------------------
-- RESET DOS AUTO-INCREMENTOS
-- Garante que novos inserts via JPA não colidam com os IDs acima.
-- -------------------------------------------------------------
ALTER TABLE usuario           ALTER COLUMN id RESTART WITH 100;
ALTER TABLE corretor          ALTER COLUMN id RESTART WITH 100;
ALTER TABLE endereco          ALTER COLUMN id RESTART WITH 100;
ALTER TABLE caracteristica    ALTER COLUMN id RESTART WITH 100;
ALTER TABLE imovel            ALTER COLUMN id RESTART WITH 100;
ALTER TABLE imagem            ALTER COLUMN id RESTART WITH 100;
ALTER TABLE mensagem_contato  ALTER COLUMN id RESTART WITH 100;
