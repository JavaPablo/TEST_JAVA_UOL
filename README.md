# Email Management API

Uma API RESTful desenvolvida em Spring Boot para gerenciar caixas de e-mail, pastas e mensagens.

## Índice

- [Descrição](#descrição)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Endpoints](#endpoints)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Descrição

Esta API permite a gestão de caixas de e-mail, pastas e mensagens armazenadas. Ela fornece funcionalidades para criar, ler, atualizar e excluir e-mails e pastas.

## Pré-requisitos

Antes de começar, você precisa ter instalado em sua máquina:

- [Java JDK 11 ou superior](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

## Instalação

1. Clone este repositório:

   ```bash
   git clone https://github.com/seu-usuario/email-management-api.git
   cd email-management-api

2. Instale as dependências:

    ```bash
    mvn install

Também poderá ser executado o comando, abaixo e ter acesso a aplicação e o banco de dados no docker!
    ```bash
    docker compose up


Como Executar:

    ```bash
    mvn spring-boot:run

A aplicação estará disponível em http://localhost:8080/swagger-ui/index.html#.

3. Endpoints

Gestão de Caixas de E-mail:

    GET /emails: Retorna todas as caixas de e-mail.
    POST /emails: Cria uma nova caixa de e-mail.
    GET /emails/{id}: Retorna uma caixa de e-mail específica.
    PUT /emails/{id}: Atualiza uma caixa de e-mail existente.

Gestão de Pastas:

    GET /folders: Retorna todas as pastas.
    POST /folders: Cria uma nova pasta.
    GET /folders/{id}: Retorna uma pasta específica.
    PUT /folders/{id}: Atualiza uma pasta existente.

Gestão de Mensagens

    GET /messages: Retorna todas as mensagens.
    POST /messages: Cria uma nova mensagem.
    GET /messages/{id}: Retorna uma mensagem específica.
    PUT /messages/{id}: Atualiza uma mensagem existente.

4. Contribuição

        Contribuições são bem-vindas! Sinta-se à vontade para enviar um pull request ou abrir uma issue.

5. Licença

         Este projeto está licenciado sob a MIT License.

