# Guia de Execução e Documentação Técnica

Este documento detalha como configurar, executar e testar o desafio de desenvolvimento, além de explicar as principais decisões arquiteturais tomadas.

## Como Rodar o Projeto

O sistema é composto por uma API REST desenvolvida em Spring Boot e uma interface web em Vue.js.

### 1. Backend (Spring Boot)

- **Tecnologias:** Java 21, Maven, PostgreSQL.
- **Configuração de Banco:** Certifique-se de que as variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` estão configuradas no seu ambiente ou no arquivo `.env`.
- **Execução:**
  ```bash
  # Na pasta raiz do projeto Java
  ./mvnw spring-boot:run
  ```
- **URL Base:** [http://localhost:8080/api](http://localhost:8080/api).

### 2. Frontend (Vue.js)

- **Tecnologias:** Vue 3, Vite, Axios.
- **Execução:**
  ```bash
  # Na pasta do projeto frontend
  npm install
  npm run dev
  ```
- **URL Base:** Geralmente [http://localhost:5173](http://localhost:5173).

## Integração e CORS

Um ponto crucial da integração é a política de CORS (Cross-Origin Resource Sharing). Como o Frontend e o Backend rodam em portas diferentes, foi necessária uma configuração para permitir a troca de dados:

- **Configuração:** Foi aplicada a anotação `@CrossOrigin(origins = "*")` tanto no `TimeController` quanto no `IntegranteController`.
- **Objetivo:** Isso permite que o navegador aceite as requisições enviadas pelo serviço Axios do Vue.js (rodando na porta 5173) para a API REST (rodando na porta 8080), garantindo que as operações de cadastro e consulta funcionem sem bloqueios de segurança.

## Funcionalidades Implementadas

### 1. Cadastro de Dados

- **Integrantes:** Cadastro completo validado via `IntegranteDTO` com o uso de `@Valid` no controller.
- **Times:** Montagem de equipes associando múltiplos integrantes. A resposta da API utiliza o `TimeResponseDTO` para retornar apenas os nomes únicos, evitando duplicidade visual no frontend.

### 2. Processamento de Dados (ApiService)

Foram implementados todos os requisitos lógicos solicitados no `ApiService`:

- **Time da Data:** Retorna a composição exata de uma data específica.
- **Integrante Mais Usado:** Identifica o jogador com mais participações em um período.
- **Time Mais Comum:** Analisa combinações repetidas de jogadores.
- **Estatísticas:** Contagem e identificação de funções e franquias mais famosas através de filtros de período inteligentes.

### 3. Tratamento de Exceções

O sistema utiliza exceções customizadas para fornecer retornos claros ao usuário:

- **SemDataException:** Disparada quando uma data obrigatória não é informada.
- **TimeNaoEncontradoException:** Quando não existem registros para a busca realizada.
- **PeriodoSemDadosException:** Para consultas estatísticas em períodos vazios.
