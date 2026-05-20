# ClinicalAgenda

Sistema completo de agendamento de consultas médicas — API REST com Spring Boot e frontend Angular, desenvolvido como projeto de portfólio com foco em boas práticas de engenharia de software.

![Java](https://img.shields.io/badge/Java_25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular_18-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens)

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Stack](#stack)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Como rodar](#como-rodar)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [API Reference](#api-reference)
- [Testes](#testes)

---

## Sobre o projeto

O ClinicalAgenda é uma aplicação full-stack para gestão de consultas médicas com controle de acesso por perfil (RBAC). O sistema permite que pacientes agendem consultas, médicos gerenciem e editem os registros, e administradores tenham controle total sobre o sistema.

Desenvolvido com foco em:

- **Clean Architecture** — separação rígida entre regras de negócio e detalhes de infraestrutura
- **Segurança** — autenticação JWT com roles, CORS configurado, senhas com BCrypt
- **Qualidade** — testes de integração com cobertura de segurança, JaCoCo para relatório
- **Observabilidade** — Spring Boot Actuator com endpoints de health e métricas
- **Notificações** — e-mail assíncrono via `@Async` ao agendar consulta (MailHog em dev)
- **Frontend** — Angular 18 standalone com Tailwind CSS, guards e interceptor JWT

---

## Stack

### Backend
| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 25 | Linguagem principal |
| Spring Boot | 4.0.2 | Framework web |
| Spring Security | 7 | Autenticação e autorização |
| Spring Data JPA | — | Persistência |
| PostgreSQL | 15 | Banco de dados |
| Flyway | — | Migrations |
| Auth0 JWT | — | Geração e validação de tokens |
| Spring Mail | — | Notificações por e-mail |
| Spring Actuator | — | Health check e métricas |
| Lombok | — | Redução de boilerplate |
| Swagger / OpenAPI | — | Documentação da API |

### Frontend
| Tecnologia | Versão | Uso |
|---|---|---|
| Angular | 18 | Framework SPA |
| Tailwind CSS | 3 | Estilização |
| TypeScript | 5.4 | Linguagem |
| Angular Signals | — | Estado reativo |
| Angular Router | — | Roteamento com guards |

### Infraestrutura
| Ferramenta | Uso |
|---|---|
| Docker / Docker Compose | Banco + MailHog em container |
| MailHog | Servidor SMTP para desenvolvimento |
| JaCoCo | Cobertura de testes |
| GitHub Actions | CI com testes automáticos |

---

## Arquitetura

O backend segue **Clean Architecture** com dependências fluindo de fora para dentro:

```
Presentation → Infrastructure → Core
```

```
src/main/java/dev/matheus/ClinicalAgenda/
├── core/
│   ├── entities/        # Entidades de domínio (records imutáveis)
│   ├── enums/           # TipoConsulta, UserRole
│   ├── exceptions/      # Exceções de negócio tipadas
│   ├── gateway/         # Contratos de acesso a dados
│   ├── dtos/            # PaginaResponse<T> (sem dependência Spring)
│   └── usecases/        # Um caso de uso por operação
│
└── infra/
    ├── persistence/     # Entidades JPA + repositories Spring Data
    ├── gateway/         # Implementações dos contratos do core
    ├── mapper/          # Conversão domain ↔ entity ↔ DTO
    ├── presentation/    # Controllers REST
    ├── security/        # JWT filter, TokenService, SecurityConfig
    ├── notifications/   # EmailService (@Async)
    ├── config/          # AsyncConfig
    ├── beans/           # BeanConfiguration (wiring dos use cases)
    └── exceptions/      # ControllerExceptionHandler (RFC 7807)
```

### Regras de negócio (enforced nos use cases)
- `dataInicio` deve ser no futuro
- Duração mínima de 15 minutos
- Sem sobreposição de médico (CRM) no mesmo horário
- Sem sobreposição de sala (consultório) no mesmo horário
- Identificador gerado automaticamente no formato `CONS-{ANO}-{SEQUENCIAL}`

---

## Funcionalidades

### Backend
- [x] Registro e login com JWT
- [x] RBAC — 3 perfis: PACIENTE, MEDICO, ADMIN
- [x] Agendar consulta com validação de conflito de horário
- [x] Listar consultas paginadas
- [x] Buscar consulta por identificador
- [x] Alterar consulta (MEDICO)
- [x] Cancelar consulta (ADMIN)
- [x] E-mail de confirmação assíncrono ao agendar
- [x] Respostas de erro RFC 7807 (Problem Details)
- [x] Auditoria automática (createdAt / updatedAt)
- [x] Health check e métricas via Actuator

### Frontend
- [x] Login e registro com seleção de perfil
- [x] Dashboard com estatísticas e consultas recentes
- [x] Agendar consulta (PACIENTE)
- [x] Listar consultas paginadas (MEDICO / ADMIN)
- [x] Detalhe da consulta com edição (MEDICO) e cancelamento (ADMIN)
- [x] Sidebar com menu dinâmico por perfil
- [x] Interceptor JWT automático em todas as requisições
- [x] Guards de rota (auth / guest)
- [x] Tema claro com design refinado

---

## Como rodar

### Pré-requisitos
- Java 17+ e Maven (ou use o `./mvnw` incluído)
- Node.js 18+ e npm
- Docker e Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/ClinicalAgenda.git
cd ClinicalAgenda
```

### 2. Crie o arquivo `.env`

```bash
cp .env.example .env
```

Ou crie manualmente com o conteúdo:

```env
POSTGRES_DB=ClinicalAgenda
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
JWT_SECRET=minha-chave-secreta-longa-aqui
```

### 3. Suba os serviços de infraestrutura

```bash
docker-compose up -d postgres mailhog
```

### 4. Inicie o backend

```bash
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

### 5. Inicie o frontend

```bash
cd frontend
npm install
ng serve
```

O frontend ficará disponível em `http://localhost:4200`.

### Serviços disponíveis

| Serviço | URL |
|---|---|
| API REST | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health Check | http://localhost:8080/actuator/health |
| MailHog (e-mails) | http://localhost:8025 |
| Frontend | http://localhost:4200 |

---

## Variáveis de ambiente

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `POSTGRES_DB` | Nome do banco | `ClinicalAgenda` |
| `POSTGRES_USER` | Usuário do banco | `postgres` |
| `POSTGRES_PASSWORD` | Senha do banco | `postgres` |
| `JWT_SECRET` | Chave de assinatura JWT | fallback hardcoded (apenas dev) |
| `MAIL_HOST` | Host SMTP | `localhost` |
| `MAIL_PORT` | Porta SMTP | `1025` |

---

## API Reference

### Autenticação

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "login": "usuario@email.com",
  "senha": "senha123",
  "role": "PACIENTE"  // PACIENTE | MEDICO | ADMIN
}
```

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "login": "usuario@email.com",
  "senha": "senha123"
}

// Response: { "token": "eyJ..." }
```

### Consultas

> Todos os endpoints abaixo requerem `Authorization: Bearer {token}`

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| `POST` | `/api/v1/consultas/agendar` | PACIENTE | Agendar consulta |
| `GET` | `/api/v1/consultas/listar` | MEDICO, ADMIN | Listar paginado |
| `GET` | `/api/v1/consultas/listar/{id}` | Autenticado | Buscar por identificador |
| `PUT` | `/api/v1/consultas/alterar/{id}` | MEDICO | Alterar consulta |
| `DELETE` | `/api/v1/consultas/cancelar/{id}` | ADMIN | Cancelar consulta |

#### Exemplo — agendar consulta

```bash
curl -X POST http://localhost:8080/api/v1/consultas/agendar \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteNome": "João Silva",
    "pacienteEmail": "joao@email.com",
    "descricaoSintomas": "Dor de cabeça frequente",
    "dataInicio": "2026-06-15T09:00:00",
    "dataFim": "2026-06-15T10:00:00",
    "consultorio": "Sala 101",
    "crmMedico": "CRM-SP-001",
    "tipo": "PRESENCIAL"
  }'
```

#### Erros — formato RFC 7807

```json
{
  "type": "https://clinica.com/erros/conflito-agenda",
  "title": "Conflito de agenda",
  "status": 409,
  "detail": "Médico já possui consulta nesse horário.",
  "timestamp": "2026-05-20T10:00:00",
  "errorCode": "CLINICAL-002"
}
```

---

## Testes

```bash
# Rodar todos os testes
./mvnw test

# Relatório de cobertura (JaCoCo)
# Após rodar os testes, abrir: target/site/jacoco/index.html
```

O projeto conta com testes de integração cobrindo:
- Registro e login de usuários
- Bloqueio de duplicatas e credenciais inválidas
- Agendamento com validação de RBAC (401 / 403)
- Validação de campos obrigatórios (400)
- Busca e cancelamento de consultas inexistentes (404)
- Listagem paginada

---

## CI/CD

O projeto possui pipeline no **GitHub Actions** (`.github/workflows/ci.yml`) que executa a cada push/PR na branch `main`:

1. Sobe um serviço PostgreSQL 15
2. Executa `./mvnw test`
3. Publica o relatório JaCoCo como artefato

---

> Projeto desenvolvido por **Matheus** como portfólio de engenharia de software.
