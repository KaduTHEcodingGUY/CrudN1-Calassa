# CrudN1-Calassa

Visão Geral da Aplicação
A aplicação é um sistema de Gerenciamento de Alunos que permite criar, ler, atualizar e excluir registros de alunos. Ela utiliza as seguintes tecnologias:

Backend: Spring Boot, um framework Java que simplifica o desenvolvimento de aplicações web e microserviços.

Acesso a Dados: Spring Data JPA, que facilita a implementação de camadas de acesso a dados.

Banco de Dados: PostgreSQL, hospedado no Supabase.

Frontend: HTML, CSS e JavaScript, criando uma interface de usuário de página única para interagir com o backend.

Estrutura do Projeto
O projeto segue a estrutura padrão de uma aplicação Spring Boot:

src/main/java: Contém o código-fonte da aplicação.

com.example.CrudN1: Pacote principal.

CrudN1Application.java: Ponto de entrada da aplicação Spring Boot.

controller: Contém as classes que expõem os endpoints da API REST (AlunoController).

model: Define as entidades do banco de dados (Aluno e User).

repository: Interfaces para acesso ao banco de dados (AlunoRepository e UserRepository).

src/main/resources: Contém os arquivos de configuração e estáticos.

application.properties: Arquivo de configuração da aplicação.

static/index.html: A interface do usuário da aplicação.

pom.xml: Define as dependências do projeto e as configurações de build do Maven.

Backend
Modelos de Dados
Aluno.java: Representa um aluno com os seguintes campos: id, createdAt, nome, email, matricula, telefone, escola, turma e turno.

User.java: Embora exista um modelo de User, parece ser um resquício ou uma funcionalidade não totalmente implementada, já que o foco principal é o CRUD de Aluno.

Repositórios
AlunoRepository.java: Estende JpaRepository, fornecendo métodos para operações de banco de dados no Aluno, como existsByEmail, existsByMatricula, e uma busca customizada por nome ou email.

UserRepository.java: Semelhante ao AlunoRepository, fornece operações de banco de dados para a entidade User.

Controlador
AlunoController.java: Expõe uma API REST para o gerenciamento de alunos:

GET /api/alunos: Lista todos os alunos ou filtra por nome/email.

GET /api/alunos/{id}: Obtém um aluno por ID.

POST /api/alunos: Cria um novo aluno, com validação para campos obrigatórios e checagem de email e matrícula duplicados.

PUT /api/alunos/{id}: Atualiza os dados de um aluno existente.

DELETE /api/alunos/{id}: Exclui um aluno.

Frontend
O arquivo index.html contém a interface do usuário e a lógica para interagir com a API do backend:

Interface: A página exibe um formulário para criar novos alunos, um campo de busca e uma tabela para listar os alunos.

Funcionalidades:

Criação: O formulário envia uma requisição POST para /api/alunos.

Listagem e Busca: A tabela é preenchida com os dados dos alunos, e a busca filtra os resultados.

Edição: Um modal é aberto para editar os dados de um aluno, enviando uma requisição PUT.

Exclusão: Um botão em cada linha da tabela permite excluir o aluno correspondente com uma requisição DELETE.

Estilo: A página tem um tema escuro e um design limpo e moderno.

Configuração
O arquivo application.properties define as configurações da aplicação, incluindo:

Nome da Aplicação: CrudN1

Fonte de Dados: Configura a conexão com um banco de dados PostgreSQL no Supabase.

JPA/Hibernate: ddl-auto=update permite que o Hibernate atualize o esquema do banco de dados automaticamente.

Porta do Servidor: A aplicação roda na porta 8081.

Em resumo, o projeto é uma aplicação web completa, bem estruturada, que demonstra as funcionalidades básicas de um sistema CRUD. Ele utiliza tecnologias modernas e segue as melhores práticas de desenvolvimento com o ecossistema Spring.
