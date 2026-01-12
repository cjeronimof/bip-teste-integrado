# Frontend Module (Angular)

Interface de usuário construída com Angular 17 e Angular Material.

## 🛠️ Tecnologias
- Angular 17
- TypeScript 5
- RxJS
- Angular Material
- Docker (Nginx)

## 🚀 Execução Local

1. Instalar dependências:
   ```bash
   npm install
   ```

2. Executar servidor de desenvolvimento:
   ```bash
   npm start
   ```
   Acesse em `http://localhost:4200`.

3. Executar Testes:
   ```bash
   npm test
   ```

## 🐳 Docker
O frontend é compilado em *Multi-stage build* e servido por um servidor Nginx leve em produção.

```bash
docker build -t frontend .
```
