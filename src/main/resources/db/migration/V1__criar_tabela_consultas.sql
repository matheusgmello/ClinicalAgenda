CREATE TABLE Consultas (
                           id BIGSERIAL PRIMARY KEY,
                           paciente_nome VARCHAR(255) NOT NULL,
                           descricao_sintomas TEXT,
                           identificador VARCHAR(255) NOT NULL UNIQUE,
                           data_inicio TIMESTAMP NOT NULL,
                           data_fim TIMESTAMP NOT NULL,
                           consultorio VARCHAR(255) NOT NULL,
                           crm_medico VARCHAR(20) NOT NULL,
                           img_receita_url VARCHAR(255),
                           tipo VARCHAR(50) NOT NULL
);