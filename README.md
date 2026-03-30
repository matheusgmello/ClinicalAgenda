# ClinicalAgenda

API REST para gerenciamento de agendamento de consultas clínicas, desenvolvida com Java e Spring Boot seguindo a arquitetura limpa.

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Configuração](#configuração)
- [Passo a Passo](#passo-a-passo)
- [Endpoints](#endpoints)

## Sobre o projeto

O ClinicalAgenda é uma API para gerenciamento de consultas médicas, permitindo agendar, listar, buscar, cancelar e remover consultas. O projeto foi desenvolvido com foco em:

- Organização das regras de negócio em camadas (clean architecture)
- Persistência e migrações de banco (Flyway)
- Segurança robusta com JWT e controle de acesso (RBAC)
- Padronização de respostas de erro (RFC 7807)
- Implantação facilitada com Docker / docker-compose

## Tecnologias

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-%23007ACC.svg?style=for-the-badge&logo=flyway&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

## Arquitetura

O projeto segue uma organização por pacotes com responsabilidades separadas:

`src/main/java/dev/matheus/ClinicalAgenda/`
- `core/` — Regras de negócio puras, entidades, enums e contratos (gateways)
- `infra/` — Implementações técnicas, persistência, segurança, mappers e controllers

Principais conceitos:
- Use Cases para representação de intenções do sistema
- Segurança desacoplada da lógica de negócio principal
- Auditoria automática de registros (createdAt e updatedAt)
- Migrations Flyway para evolução controlada do banco de dados

## Funcionalidades

- Agendar consultas com validação de conflito de horário (médico e sala)
- Controle de acesso baseado em Roles (PACIENTE, MEDICO, ADMIN)
- Autenticação e Autorização via JWT
- Tratamento de erros detalhado seguindo a RFC 7807 (Problem Details)
- Listar, buscar e cancelar consultas de forma segura
- Auditoria de criação e atualização de registros

## Configuração

Arquivos relevantes:

- `src/main/resources/application.yaml` — configurações da aplicação e segurança
- `src/main/resources/db/migration/` — histórico de migrações (Consultas, Auditoria, Usuários)
- `Docker-compose.yml` — orquestração com serviços necessários

Exemplo de configuração do datasource (application.yaml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/clinicalagenda
    username: seu_usuario
    password: sua_senha
  flyway:
    enabled: true
api:
  security:
    token:
      secret: ${JWT_SECRET}
```

## Passo a Passo

1. Clone o repositório:

```bash
git clone [url-do-repositorio]
cd ClinicalAgenda
```

2. Docker Compose:

```bash
docker-compose up -d
```

3. Build do projeto:

```bash
./mvnw clean package
```

4. Iniciar a aplicação:

```bash
java -jar target/*.jar
```

## Endpoints

### Autenticação
- `POST /api/v1/auth/register` — Criar novo usuário (Roles: PACIENTE, MEDICO, ADMIN)
- `POST /api/v1/auth/login` — Autenticar e obter token JWT

### Consultas
Base URL: `/api/v1/consultas`

- `POST /agendar` (Role: PACIENTE) — Agendar consulta
- `GET /listar` (Role: MEDICO, ADMIN) — Listar consultas
- `PUT /alterar/{id}` (Role: MEDICO) — Alterar sintomas
- `DELETE /cancelar/{id}` (Role: ADMIN) — Cancelar consulta

Exemplo de Agendamento (requer Header Authorization):

```bash
curl -X POST http://localhost:8080/api/v1/consultas/agendar \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteNome": "João Silva",
    "descricaoSintomas": "Dor de cabeça",
    "dataInicio": "2026-03-25T10:00:00",
    "dataFim": "2026-03-25T10:30:00",
    "consultorio": "Sala 101",
    "crmMedico": "CRM123",
    "tipo": "PRESENCIAL"
  }'
```
