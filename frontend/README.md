# Frontend - Interface Web

Interface web para gerenciamento de alunos com design responsivo e moderno.

## 🚀 Como executar

### Opção 1: Abrir diretamente
Abra o arquivo `src/index.html` em seu navegador.

### Opção 2: Servidor local (recomendado)
```bash
# Usando Python
python -m http.server 8000

# Usando Node.js
npx serve src

# Usando PHP
php -S localhost:8000 -t src
```

Acesse: `http://localhost:8000`

## 🎨 Funcionalidades

- ✅ **Interface moderna** com tema escuro
- ✅ **Design responsivo** para desktop e mobile
- ✅ **CRUD completo** de alunos
- ✅ **Busca em tempo real** por nome ou email
- ✅ **Validação de formulários**
- ✅ **Modal de edição** intuitivo
- ✅ **Feedback visual** para ações do usuário

## 🛠️ Tecnologias

- **HTML5** - Estrutura semântica
- **CSS3** - Estilos modernos com CSS Grid e Flexbox
- **JavaScript (Vanilla)** - Lógica da aplicação
- **Fetch API** - Comunicação com o backend

## 📱 Recursos da Interface

### Tabela de Alunos
- Listagem completa com todos os campos
- Ordenação visual por colunas
- Hover effects e transições suaves
- Scroll horizontal em telas pequenas

### Formulário de Criação
- Validação em tempo real
- Campos obrigatórios destacados
- Reset automático após criação

### Modal de Edição
- Preenchimento automático dos campos
- Validação antes do envio
- Botões de ação intuitivos

### Busca
- Busca por nome ou email
- Resultados em tempo real
- Botão de limpar filtros

## 🔗 Integração com Backend

A interface se conecta com a API Spring Boot através dos endpoints:
- `GET /api/alunos` - Listar alunos
- `POST /api/alunos` - Criar aluno
- `PUT /api/alunos/{id}` - Atualizar aluno
- `DELETE /api/alunos/{id}` - Deletar aluno

## 🎯 Configuração

Para conectar com um backend em outra porta, edite a variável `api` no JavaScript:

```javascript
const api = 'http://localhost:8080/api/alunos';
```

## 📱 Responsividade

A interface se adapta a diferentes tamanhos de tela:
- **Desktop**: Layout em grid com 6 colunas
- **Tablet**: Layout adaptado com colunas menores
- **Mobile**: Layout vertical com scroll horizontal
