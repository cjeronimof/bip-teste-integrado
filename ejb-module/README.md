# ejb-module

Este é o **coração** da aplicação. Ele encapsula toda a lógica de negócio e as regras do domínio.

## 📦 Estrutura

- **`domain`**: Entidades de negócio (Ex: `Beneficio`).
    - Regras de validação de negócio intrínsecas (Ex: métodos `debitar` e `creditar`).
- **`application`**: Casos de uso.
    - **`port.in`**: Interfaces que definem o QUE o sistema faz (`TransferenciaUseCase`).
    - **`port.out`**: Interfaces que definem O QUE o sistema precisa (`BeneficioRepositoryPort`).
    - **`service`**: Implementação dos casos de uso (`TransferenciaService`).

## 🧪 Testes
Os testes aqui são **Unitários** e focados na regra de negócio. Usei **Mockito** para simular as portas de saída.

```bash
mvn test
```
