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
- Implantação facilitada com Docker / docker-compose

## Tecnologias

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-%23007ACC.svg?style=for-the-badge&logo=flyway&logoColor=white)

## Arquitetura

O projeto segue uma organização por pacotes com responsabilidades separadas:

`src/main/java/dev/matheus/ClinicalAgenda/`
- `core/` — Regras de negócio (entities, enums, usecases, gateway)
- `infra/` — Implementações da infraestrutura (persistence, gateway, mapper, presentation, dtos, exceptions)

Principais conceitos:
- Use Cases que representam as operações da aplicação (agendar, listar, buscar, cancelar)
- Gateways para abstrair acesso à persistência
- DTOs e mappers para conversão entre entidade e representação HTTP
- Migrations Flyway em `src/main/resources/db/migration` (ex.: `V1__criar_tabela_consultas.sql`)

## Funcionalidades

- Agendar consultas (validando conflito de horário e dados obrigatórios)
- Listar todas as consultas
- Buscar consulta por identificador
- Cancelar consultas (ação explícita de cancelamento)
- Mapeamento entre entidade e DTO para comunicação via REST
- Migrations de banco para manter histórico de esquema

## Configuração

Arquivos relevantes:

- `src/main/resources/application.yaml` — configurações da aplicação
- `src/main/resources/db/migration/V1__criar_tabela_consultas.sql` — migração inicial (Flyway)
- `Docker-compose.yml` — orquestração com serviços necessários (ex.: banco)

Exemplo de configuração do datasource (application.yaml ou application.properties):

```
spring.datasource.url=jdbc:postgresql://localhost:5432/clinicalagenda
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

## Passo a Passo

1. Clone o repositório:

```
git clone [url-do-repositorio]
cd ClinicalAgenda
```

2.  Docker Compose:

```
docker-compose up -d
```

3. Build do projeto (Unix/macOS):

```
./mvnw clean package
```

ou, em Windows (PowerShell/CMD):

```
./mvnw.cmd clean package
```

4. Iniciar a aplicação (após build):

```
java -jar target/*.jar
```

A API ficará disponível em `http://localhost:8080` por padrão.

## Endpoints

O projeto expõe os endpoints a partir da base path `api/v1/consultas`. Abaixo estão os caminhos reais extraídos de `ConsultaController` e exemplos de cURL para testar cada operação.

Base URL (local):

```
http://localhost:8080/api/v1/consultas
```

Agendar consulta

- `POST /agendar` — Agendar nova consulta

Exemplo cURL:

```bash
curl -X POST http://localhost:8080/api/v1/consultas/agendar \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteNome": "João Silva",
    "descricaoSintomas": "Dor de cabeça e febre",
    "identificador": "123e4567-e89b-12d3-a456-426614174000",
    "dataInicio": "2026-03-01T10:30:00",
    "dataFim": "2026-03-01T10:50:00",
    "consultorio": "Sala 12",
    "crmMedico": "CRM12345",
    "imgReceitaUrl": "https://exemplo.com/receita.jpg",
    "tipo": "PRESENCIAL"
  }'
```

Listar consultas

- `GET /listar` — Listar todas as consultas

Exemplo cURL:

```bash
curl http://localhost:8080/api/v1/consultas/listar
```

Buscar consulta por identificador

- `GET /listar/{identificador}` — Buscar consulta por identificador

Exemplo cURL:

```bash
curl http://localhost:8080/api/v1/consultas/listar/123e4567-e89b-12d3-a456-426614174000
```

Cancelar consulta

- `DELETE /cancelar/{identificador}` — Cancelar consulta por identificador

Exemplo cURL:

```bash
curl -X DELETE http://localhost:8080/api/v1/consultas/cancelar/123e4567-e89b-12d3-a456-426614174000
```
