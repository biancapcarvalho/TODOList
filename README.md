# TODO List - Gerenciador de Tarefas em Java
_**Desenvolvido por Bianca Carvalho**_

---

---

## Sobre o projeto

O projeto é uma aplicação TODO List que atualmente está dividida em Frontend e Backend, que por enquanto não se comunicam.

O backend opera via terminal e gerencia a lógica de negócios e estruturação dos dados em Java puro.

O frontend apresenta uma interface gráfica intuitiva e responsiva em estilo Kanban, simulando as operações de CRUD através do JavaScript no navegador, sem comunicação real com o backend ou persistência de dados.

---

## Tecnologias utilizadas

### Frontend
- HTML5, CSS3 e JavaScript
- Bootstrap 5.3.8
- FontAwesome

### Backend
- Java JDK 17.0.18 - Sem uso de frameworks
- Spock 2.4 (Groovy 5.0) - Para testes unitários

---

## Funcionalidades

### Frontend
- **Quadro Kanban:** Visualização das tarefas divididas em colunas por status: A Fazer (TODO), Em Progresso (DOING) e Concluído (DONE).
- **Gestão de Tarefas (Simulada):** Permite Criar (C), Ler (R), Atualizar (U) e Deletar (D) tarefas usando JavaScript.
- **Ordenação Automática:** As tarefas são exibidas respeitando a ordem de prioridade (1 a 5).

Obs.: A gestão de tarefas está sendo feita pelo frontend para simular o comportamento do backend enquanto não há integração entre os dois.

### Backend
- **Menu**: Menu com as opções de ver, criar, atualizar e excluir tarefas.
- **Criar Tarefa**: Permite cadastrar tarefas com:
  - Título*, Descrição, Data de Término, Prioridade (1 a 5)*, Categoria (Casa, Faculdade, Trabalho), e Status (Todo, Doing, Done)*
- **Balanceamento**: Novas tarefas são adicionadas a lista respeitando a ordenação por prioridade
- **Visualizar lista de tarefas**:
  - Tem opções para visualizar a lista filtrada por Status, Categoria e Prioridade
  - A lista é ordenada por prioridade
- **Atualizar tarefa**: Permite a edição de tarefas (utiliza ID e shallow copy)
- **Remover Tarefa**: Permite a exclusão de tarefas (utiliza ID)
- **Validação de Dados**: Tratamento de exceções para entradas inválidas (fora do TaskService, que é responsável somente pela lógica de manipulação das tarefas), e uso de números para facilitar a identificação e seleção de uma opção pelo usuário

----

## Estrutura do projeto

- `/Frontend`: Contém os arquivos de interface (`index.html`, `style.css`, `script.js`).
- `/src`: Contém todo o código-fonte do Backend Java.
  - `Model/`: Entidades como Task e Status.
  - `Service/`: Lógica de manipulação e tratamento de dados.
  - `Repository/`: Manipulação da lista de tarefas.
  - `View/Menu`: Interação com o usuário no terminal.
  - `Main`: Ponto de entrada da aplicação.
- `/test`: testes unitários para TaskService e TaskRepository

---

## Como executar

Antes tudo é necessário clonar o repositório git:
```bash
git clone https://github.com/biancapcarvalho/TODOList.git
```

#### Executar o frontend
Para executar o frontend você deve abrir o arquivo `index.html` em um navegador.

### Executar o backend via IntelliJ
Inicie/abra o projeto no IntelliJ, vá no arquivo src/Main.java e execute o programa (shift+f10)

### Executar o backend via terminal
Vá até o diretório `src` dentro do repositório clonado
```bash
cd TODOList/src
```

Compile o arquivo de entrada da aplicação (Main.java)
```bash
javac Main.java
```

Execute a aplicação
```bash
java Main
```

Após isso será exibido no terminal o menu principal daa aplicação, como mostra a tela abaixo.

![Execução da aplicação via termminal](terminal.png)

