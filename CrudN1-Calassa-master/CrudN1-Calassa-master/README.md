# 🎓 CrudN1-Calassa - Gerenciador de Alunos

Sistema CRUD para gerenciamento de alunos desenvolvido com **Spring Boot** e frontend **HTML/CSS/JavaScript**.

## 📋 Funcionalidades

- ✅ **Cadastro de usuários** (email e senha)
- ✅ **Login com autenticação JWT**
- ✅ **CRUD completo de alunos** (Criar, Ler, Atualizar, Deletar)
- ✅ **Busca por nome ou email**
- ✅ **Interface responsiva** com design moderno escuro

## 🏗️ Arquitetura

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Frontend      │────▶│   Auth Server   │     │   PostgreSQL    │
│   (HTML/JS)     │     │   (porta 8082)  │     │   (Supabase)    │
└────────┬────────┘     └─────────────────┘     └────────▲────────┘
         │                                               │
         │              ┌─────────────────┐              │
         └─────────────▶│   Backend API   │──────────────┘
                        │   (porta 8081)  │
                        └─────────────────┘
```

## 🔐 Auth Server

Este projeto depende de um **Auth Server** externo para autenticação. O auth-server deve estar rodando em:

```
http://localhost:8082/auth-server
```

### Endpoints do Auth Server utilizados:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/auth/login` | Login de usuário |
| `POST` | `/auth/register` | Cadastro de novo usuário |
| `GET` | `/auth/me` | Dados do usuário autenticado |
| `GET` | `/oauth2/jwks` | Chaves públicas para validação JWT |

### Formato das requisições:

**Login:**
```json
POST /auth/login
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

**Cadastro:**
```json
POST /auth/register
{
  "email": "usuario@email.com",
  "password": "senha123",
  "roles": "USER"
}
```

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.8+
- Auth Server rodando na porta 8082

### 1. Clone o repositório

```bash
git clone https://github.com/KaduTHEcodingGUY/CrudN1-Calassa.git
cd CrudN1-Calassa
```

### 2. Inicie o Auth Server

Certifique-se de que o Auth Server está rodando em `http://localhost:8082/auth-server`

### 3. Execute a aplicação

```bash
mvn spring-boot:run
```

Ou via IDE (IntelliJ, Eclipse, VS Code):
- Execute a classe `CrudN1Application.java`

### 4. Acesse a aplicação

- **Login:** http://localhost:8081/login.html
- **CRUD:** http://localhost:8081/index.html (requer autenticação)

## 📁 Estrutura do Projeto

```
src/main/
├── java/com/example/CrudN1/
│   ├── config/
│   │   └── SecurityConfig.java      # Configuração de segurança
│   ├── controller/
│   │   ├── AlunoController.java     # API REST de alunos
│   │   ├── AuthController.java      # Proxy para auth (opcional)
│   │   └── UserController.java      # Gerenciamento de usuários
│   ├── model/
│   │   ├── Aluno.java               # Entidade Aluno
│   │   └── User.java                # Entidade User
│   ├── repository/
│   │   ├── AlunoRepository.java     # Repositório de alunos
│   │   └── UserRepository.java      # Repositório de usuários
│   ├── service/
│   │   └── AuthService.java         # Serviço de autenticação
│   └── CrudN1Application.java       # Classe principal
└── resources/
    ├── static/
    │   ├── index.html               # Tela do CRUD
    │   └── login.html               # Tela de Login/Cadastro
    └── application.properties       # Configurações
```

## 🔧 Configuração

### application.properties

```properties
# Porta da aplicação
server.port=8081

# Auth Server URL
auth.server.url=http://localhost:8082/auth-server

# Validação JWT
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8082/auth-server/oauth2/jwks
```

## 🛡️ Segurança

- **Autenticação:** JWT Bearer Token
- **Validação:** Tokens são validados usando JWK do Auth Server
- **Endpoints públicos:** `/login.html`, `/api/auth/**`
- **Endpoints protegidos:** `/api/alunos/**` (requer token válido)

## 📱 Telas

### Login
- Campo de email e senha
- Botão para criar conta
- Validação de campos

### Cadastro
- Email, senha e confirmação de senha
- Validação de senha (mínimo 6 caracteres)
- Redirecionamento automático para login após sucesso

### CRUD de Alunos
- Tabela com todos os alunos
- Formulário de criação
- Busca por nome/email
- Edição inline
- Exclusão com confirmação
- Botão de logout

## 🛠️ Tecnologias

- **Backend:** Spring Boot 3.3.5
- **Segurança:** Spring Security + OAuth2 Resource Server
- **Banco de Dados:** PostgreSQL (Supabase)
- **Frontend:** HTML5, CSS3, JavaScript (Vanilla)
- **Build:** Maven

## 👤 Autor

**KaduTHEcodingGUY**

## 📄 Licença

Este projeto está sob a licença MIT.
