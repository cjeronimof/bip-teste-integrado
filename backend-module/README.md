# backend-module

Este módulo é responsável por fazer a conexão entre o "Mundo Real" e o `ejb-module` (Domínio).

## 🛠️ Tecnologias
- **Spring Boot 3.2**: Framework de aplicação.
- **Spring Data JPA**: Acesso a dados.
- **H2 / PostgreSQL**: Banco de dados.
- **Docker**: Containerização.

## 📦 Estrutura

- **`web`**: Adaptadores de entrada (API REST).
    - `Controller`: Recebe requisições HTTP e chama os UseCases do Core.
    - `DTO`: Objetos de transferência de dados (Isolam o domínio do JSON).
    - `Mapper`: Converte DTO <-> Domínio.
- **`persistence`**: Adaptadores de saída (Banco de Dados).
    - `Entity`: Entidades JPA (`@Entity`) que espelham o banco.
    - `Repository`: Interface Spring Data.
    - `Adapter`: Implementa a porta `BeneficioRepositoryPort` do Core, traduzindo chamadas de domínio para chamadas JPA.
- **`config`**: Configuração do Spring.
    - Cria os Beans dos serviços do Core (que não tem anotações `@Service`).

## 🚀 Execução
Este é o módulo executável Spring Boot.

```bash
mvn spring-boot:run
```
