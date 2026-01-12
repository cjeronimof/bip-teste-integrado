# 🏗️ Desafio Fullstack Integrado
🚨 Instrução Importante (LEIA ANTES DE COMEÇAR)
❌ NÃO faça fork deste repositório.

Este repositório é fornecido como modelo/base. Para realizar o desafio, você deve:
✅ Opção correta (obrigatória)
Clique em “Use this template” (se este repositório estiver marcado como Template)
OU
Clone este repositório e crie um NOVO repositório público em sua conta GitHub.
📌 O resultado deve ser um repositório próprio, independente deste.

## 🎯 Objetivo
Criar solução completa em camadas (DB, EJB, Backend, Frontend), corrigindo bug em EJB e entregando aplicação funcional.

## 📦 Estrutura
- db/: scripts schema e seed
- ejb-module/: serviço EJB com bug a ser corrigido
- backend-module/: backend Spring Boot
- frontend/: app Angular
- docs/: instruções e critérios
- .github/workflows/: CI

## ✅ Tarefas do candidato
1. Executar db/schema.sql e db/seed.sql
2. Corrigir bug no BeneficioEjbService
3. Implementar backend CRUD + integração com EJB
4. Desenvolver frontend Angular consumindo backend
5. Implementar testes
6. Documentar (Swagger, README)
7. Submeter via fork + PR

## 🐞 Bug no EJB
- Transferência não verifica saldo, não usa locking, pode gerar inconsistência
- Espera-se correção com validações, rollback, locking/optimistic locking

## 📊 Critérios de avaliação
- Arquitetura em camadas (20%)
- Correção EJB (20%)
- CRUD + Transferência (15%)
- Qualidade de código (10%)
- Testes (15%)
- Documentação (10%)
- Frontend (10%)

## 🚀 Como Executar (Docker Compose)

A maneira mais fácil de rodar a solução completa (Banco, Backend, Frontend).

### Pré-requisitos
*   Docker & Docker Compose instalados.

### Passos
1.  Na raiz do projeto, execute:
    ```bash
    docker-compose up --build
    ```
2.  Aguarde os containers subirem.
3.  Acesse:
    *   **Frontend**: http://localhost:80
    *   **Backend API**: http://localhost:8080/api/beneficios
    *   **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## 🛠️ Como Executar (Desenvolvimento Local)

Se preferir rodar os serviços individualmente.

### Pré-requisitos
*   Java 17 (JDK)
*   Maven 3.8+
*   Node.js 18+

### 1. Build do Backend
```bash
# Na raiz do projeto
mvn clean install
```

### 2. Rodar a backend-module
O backend usa H2 (em memória) por padrão se não houver variáveis de ambiente PostgreSQL configuradas.

```bash
cd backend-module
mvn spring-boot:run
```
*   API: http://localhost:8080

### 3. Rodar o Frontend (Angular)
```bash
cd frontend
npm install
npm start
```
*   UI: http://localhost:4200

---

## 🧪 Testes

### Unitários
```bash
cd ejb-module
mvn test
```

### Integração
```bash
cd backend-module
mvn test
```

### Frontend
```bash
cd frontend
npm test
```
