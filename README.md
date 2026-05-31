# API REST Genérica com Spring Boot e MongoDB

## Integrantes

- Bianca Jennifer Franklin da Silva
- Juvam Rodrigues do Nascimento Neto
- Lucas Estanislau Gomes da Silva

---

## Descrição do Projeto

Esta aplicação consiste em uma API REST genérica desenvolvida com Spring Boot e MongoDB que permite criar, consultar, atualizar e remover documentos de qualquer coleção do banco de dados sem a necessidade de definir entidades fixas.

A API suporta:

- Criação dinâmica de coleções;
- Inserção de um ou mais documentos;
- Consulta de documentos com filtros dinâmicos;
- Projeção de campos retornados;
- Paginação de resultados;
- Atualização de documentos;
- Contagem de documentos;
- Remoção de documentos específicos ou todos;
- Tratamento de exceções personalizado.

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB
- Docker
- Docker Compose
- Maven
- BSON Document

---

## Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- Docker
- Docker Compose

---

## Como Executar o Projeto

### 1. Clonar o repositório

```bash
git clone "https://github.com/imetropoledigital/trabalho-ii-unidade-bianca-juvam-lucas.git"
```

### 2. Entrar na pasta do projeto

```bash
cd apirest
```

### 3. Construir e iniciar os containers

Na primeira execução, utilize:

```bash
docker compose up --build
```

O parâmetro `--build` é necessário apenas na primeira execução para construir as imagens Docker da aplicação e do banco de dados.

### 4. Próximas execuções

Após as imagens terem sido criadas, basta iniciar os containers normalmente:

```bash
docker compose up
```

Para executar em segundo plano:

```bash
docker compose up -d
```

### 5. Encerrar os containers

```bash
docker compose down
```

---

## Acesso à Aplicação

Após a inicialização dos containers, a API estará disponível em:

```text
http://localhost:8080
```

O MongoDB é executado automaticamente em um container Docker configurado pelo Docker Compose, não sendo necessária nenhuma instalação ou configuração manual adicional.

---

# Rotas da API

## 1. Página Inicial

### Requisição

```http
GET /
```

### Resposta

```json
{
  "status": 200,
  "message": "API Rest Java + Spring Boot com MongoDB",
  "details": ""
}
```

---

## 2. Criar Coleção e Inserir Documentos

### Requisição

```http
POST /usuarios
```

### Body

```json
[
  {
    "nome": "João Silva",
    "idade": 25,
    "cidade": "Natal"
  },
  {
    "nome": "Maria Souza",
    "idade": 30,
    "cidade": "Recife"
  }
]
```

### Resposta

```json
{
  "status": 200,
  "message": "2 documento(s) criado(s) na coleção usuarios.",
  "dado": [
    {
      "nome": "Lucas Silva",
      "idade": 22,
      "cidade": "Natal",
      "ativo": true,
      "salario": 3200.5,
      "_id": {
        "date": "2026-05-30T22:51:40.000Z",
        "timestamp": 1780181500
      }
    },
    {
      "nome": "Maria Souza",
      "idade": 25,
      "cidade": "Recife",
      "ativo": true,
      "salario": 4100.0,
      "_id": {
        "date": "2026-05-30T22:51:43.000Z",
        "timestamp": 1780181503
      }
    }
  ]
}
```

## 3. Buscar Todos os Documentos

### Requisição

```http
GET /usuarios
```

### Resposta

```json
[
  {
    "_id": "...",
    "nome": "João Silva",
    "idade": 25,
    "cidade": "Natal"
  }
]
```

---

## 4. Buscar Apenas Campos Específicos

### Requisição

```http
GET /usuarios?fields=nome,cidade
```

### Resposta

```json
[
  {
    "_id": "...",
    "nome": "João Silva",
    "cidade": "Natal"
  }
.
.
.
]
```

Essa consulta retorna apenas nome e cidade de todos os documentos da coleção "usuarios" e, por padrão, o "_id".
Para remover o "_id" da consulta, basta inserir "-_id" após os campos desejados separados por vírgula.

---

## 5. Buscar Utilizando Filtros

### Igualdade

```http
GET /usuarios?cidade=Natal
```

### Maior que

```http
GET /usuarios?idade_gt=18
```

### Maior ou igual

```http
GET /usuarios?idade_gte=18
```

### Menor que

```http
GET /usuarios?idade_lt=30
```

### Menor ou igual

```http
GET /usuarios?idade_lte=30
```

### Diferente

```http
GET /usuarios?idade_ne=25
```

### Combinando filtros

```http
GET /usuarios?cidade=Natal&idade_gte=18
```

---

## 6. Paginação

### Primeira página

```http
GET /usuarios?page=1&limit=10
```

### Segunda página

```http
GET /usuarios?page=2&limit=10
```

O limite padrão é de 100 documentos, com limite mínimo de 1 e máximo de 1000.

Caso queira visualizar os documentos em intervalo, utilize o parâmetro "page=<número_da_página_que_inicia_a_contagem>"

---

## 7. Atualizar Documento

### Requisição

```http
PUT /usuarios/684123456789abcdef123456
```

### Body

```json
{
  "nome": "João Pedro",
  "idade": 26
}
```

### Resposta

```json
{
    "status": 200,
    "message": "Documento 684123456789abcdef123456 da coleção filmes atualizado com sucesso!",
    "dado": [
        {
            "_id": "6a1b69ff5795ceb7f29987c5",
            "nome": "João Pedro",
            "idade": 26,
            "cidade": "Recife",
            "ativo": true,
            "salario": 4100.0
        }
    ]
}
```

---

## 8. Remover Documento

### Requisição

```http
DELETE /usuarios/6a1b74a75795ceb7f2998828
```

### Resposta

```json
{
    "status": 200,
    "message": "Documento 6a1b74a75795ceb7f2998828 da coleção usuarios deletado com sucesso!",
    "dado": {
        "deletedCount": 1
    }
}
```
---

## 9. Remover Todos os Documento

### Requisição

```http
DELETE deletar-todos/usuarios
```

### Resposta

```json
{
    "status": 200,
    "message": "Documentos da coleção usuarios deletados com sucesso!",
    "dado": {
        "deletedCount": 100
    }
}
```

---

## 10. Contar Documentos
### Requisição

```http
DELETE contar-documentos/usuarios
```

### Resposta

```json
{
    "status": 200,
    "message": "Contagem realizada com sucesso",
    "dado": 100
}
```

---

## Estrutura Geral do Projeto

```text
## Estrutura Geral do Projeto

```text
trabalho-ii-unidade-bianca-juvam-lucas
├── .idea
└── apirest
    ├── .idea
    ├── .mvn
    │   └── wrapper
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── br
    │   │   │       └── ufrn
    │   │   │           └── bdnosql
    │   │   │               └── apirest
    │   │   │                   ├── controller
    │   │   │                   ├── exception
    │   │   │                   │   └── custom
    │   │   │                   ├── message
    │   │   │                   └── service
    │   │   └── resources
    │   └── test
    │       └── java
    │           └── br
    │               └── ufrn
    │                   └── bdnosql
    │                       └── apirest
    └── target
        ├── generated-sources
        │   └── annotations
        └── generated-test-sources
            └── test-annotations
```

### Organização dos Pacotes

| Pacote                | Responsabilidade                                                                                                  |
| --------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `controller`          | Contém os endpoints REST responsáveis por receber e responder às requisições HTTP da API.                         |
| `service`             | Implementa as regras de negócio e realiza as operações de criação, consulta, atualização e remoção de documentos.
| `exception`           | Centraliza o tratamento global de exceções da aplicação.                                                          |
| `exception/custom`    | Contém exceções personalizadas utilizadas para validações e regras específicas do sistema.                        |
| `message`             | Classes utilizadas para padronizar as mensagens e respostas retornadas pela API.                                  |
| `resources`           | Arquivos de configuração da aplicação, como `application.yml` e demais recursos necessários para execução. |
| `test`                | Estrutura destinada aos testes automatizados da aplicação.                                                        |
| `target`              | Diretório gerado automaticamente pelo Maven durante o processo de compilação e empacotamento do projeto.          |

---

## Tratamento de Erros

A API possui tratamento global de exceções retornando respostas padronizadas.

### Exemplo

```json
{
    "status": 400,
    "message": "O número da página deve ser maior que 0.",
    "error": "Bad Request",
    "method": "GET",
    "path": "/filmes"
}
```

```json
{
    "status": 404,
    "message": "Documento não encontrado na collection produtos.",
    "error": "Not Found",
    "method": "PUT",
    "path": "/produtos/6a1b7ced437a2795a89c9415"
}
```

