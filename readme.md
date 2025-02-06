# Resumo da Aplicação

Esta aplicação é um serviço de upload de vídeos que permite aos usuários enviar vídeos para um bucket S3 da AWS. A aplicação valida tokens JWT para autenticação e armazena informações sobre os vídeos enviados em um banco de dados MySQL.

# Documentação da API

## Upload de Vídeos

### `POST /uploads/videos`

**Descrição:** Faz o upload de um vídeo.

**Parâmetros:**

-  `file` (MultipartFile): O arquivo de vídeo a ser enviado.
-  `Authorization` (String): O token JWT para autenticação.

**Resposta de Sucesso:**

-  **Código:** 200 OK
-  **Conteúdo:** `Upload realizado com sucesso! URL: {fileUrl}`

**Resposta de Erro:**

-  **Código:** 500 Internal Server Error
-  **Conteúdo:** `Erro ao enviar o arquivo: {mensagem de erro}`

# Variáveis de Ambiente

A aplicação utiliza as seguintes variáveis de ambiente:

-  `MYSQL_HOST`: Host do banco de dados MySQL.
-  `MYSQL_PORT`: Porta do banco de dados MySQL.
-  `MYSQL_DATABASE`: Nome do banco de dados MySQL.
-  `MYSQL_USER`: Usuário do banco de dados MySQL.
-  `MYSQL_PASSWORD`: Senha do banco de dados MySQL.
-  `URL_AUTH_SERVICE`: URL do serviço de autenticação.
-  `URL_STATUS_TRACKER_SERVICE`: URL do serviço de rastreamento de status.
-  `AWS_S3_ACCESS_KEY`: Chave de acesso AWS S3.
-  `AWS_S3_SECRET_KEY`: Chave secreta AWS S3.
-  `AWS_S3_SESSION_TOKEN`: Token de sessão AWS S3.
-  `AWS_S3_REGION`: Região AWS S3.
-  `AWS_S3_BUCKET_NAME`: Nome do bucket AWS S3.

# Como Rodar a Aplicação

1. Clone o repositório:

   ```bash
   git clone <URL do repositório>
   cd fh-srv-video-upload
   ```

2. Configure as variáveis de ambiente no arquivo `.env` ou exporte-as diretamente no terminal.

3. Construa e inicie os containers Docker:

   ```bash
   docker-compose up --build
   ```

4. A aplicação estará disponível em `http://localhost:8080`.

# Swagger

A documentação Swagger da API pode ser acessada em:

```
http://localhost:8080/swagger-ui.html
```
