# 📦 EstocAi
O **EstocAi** é uma **API RESTful** super completa, feita para você gerenciar os produtos, categorias e, principalmente, todas as movimentações do seu estoque (entradas e saídas) de um jeito fácil e sem estresse. Diga adeus à planilha bagunçada\! 
Esta API foi pensada para cobrir todo o ciclo de vida do seu estoque. Dê uma olhada no que ela é capaz:

### Segurança (Autenticação e Autorização)

Mantemos a casa em ordem com JWT para garantir que só gente autorizada acesse as informações:

  * **Cadastro & Login:** Você se registra e faz login, recebendo um token JWT (aquele passe mágico) para usar as rotas da API.
  * **Funções:** Temos dois níveis de usuário, **`USER`** e **`ADMIN`**, para controlar quem pode fazer o quê no sistema.

### Categorias

Organização é tudo\! Crie, edite, liste e remova as categorias dos seus produtos. Simples assim.

### Produtos

O coração do negócio\! Aqui você gerencia seus itens:

  * **Cadastro:** Adicionar produtos com nome, descrição, preço, quantidade e associar a uma categoria.
  * **Manutenção:** Editar e remover produtos para manter o catálogo atualizado.
  * **Validações:** O EstocAi não aceita quantidades ou preços negativos. É para evitar inconsistências nos dados\! 

### Fluxo de Estoque (Movimentações)

Rastreabilidade 100%\! Todas as entradas e saídas são registradas:

  * **Registrar:** Registrar toda entrada ou saída de produto, anotando data, tipo de operação e quantidade.
  * **Histórico:** O usuário pode consultar o histórico de todas as suas movimentações.
  * **Bloqueio de Saída:** Para manter a integridade, a API não permite uma saída maior do que a quantidade que está no estoque.
  * **Quem Fez?:** O administrador consegue rastrear qual usuário realizou cada movimentação.

## 🛠️ Tecnologias Envolvidas

(Abaixo, liste as tecnologias que você usou. Não esqueça de adaptar\!)

| Categoria | Tecnologia (Exemplo) |
| :---: | :---: |
| **Linguagem** | `Java 17` |
| **Framework** | `Spring Boot 3` |
| **Banco de Dados** | `MySql` |
| **Testes** | `JUnit`, `Mockito`, `MockMvc` |

## Como Colocar o EstocAi para Rodar

1.  **Clonar o Repositório:**
    ```bash
    git clone [link-do-seu-repo]
    cd EstocAi
    ```
2.  **Configurar o Ambiente:**
      * Crie seu banco de dados (ex: `estocai_db`).
      * Configure as credenciais do banco e a porta do servidor no arquivo de configuração principal (ex: `application.properties` ou `.env`).
3.  **Instalar Dependências:**
    ```bash
    # Se for Java/Maven
    mvn clean install
    ```
4.  **Executar a Aplicação:**
    ```bash
    # Se for Java/Spring Boot
    mvn spring-boot:run
    ```
5.  Pronto\! A API estará pronta para receber requisições em `http://localhost:[SUA_PORTA]`.

## Requisitos de Testes

  * **Testes Unitários:** Nos serviços, usamos o `Mockito` (ou equivalente) para isolar a lógica e garantir que ela está correta.
  * **Testes de Integração:** `MockMvc`/`TestRestTemplate` entram em ação para validar se todos os endpoints estão respondendo corretamente (incluindo autenticação, autorização e validações de entradas).


Curtiu? Se tiver alguma dúvida ou sugestão, **abre uma Issue**\! Vamos fazer o EstocAi crescer\! 💻✨
