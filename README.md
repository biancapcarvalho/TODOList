# TODO List - Gerenciador de Tarefas em Java
Repositório destinado ao ZG Hero Project da Trilha de Java

O projeto foi desenvolvido em Java puro, sem uso de qualquer framework, e roda via terminal (feito somente o backend).


## Funcionalidades

- **Menu**: Menu com as opções de ver, criar e excluir tarefas.


- **Criar Tarefa**: Permite cadastrar tarefas com:
    - Título*
    - Descrição
    - Data de Término
    - Prioridade (1 a 5)*
    - Categoria (Casa, Faculdade, Trabalho)
    - Status (Todo, Doing, Done)*


- **Balanceamento**: Novas tarefas são adicionadas a lista respeitando a ordenação por prioridade


- **Visualizar lista de tarefas**:
    - Tem opções para visualizar a lista ordenada por Status, Categoria e Prioridade, sendo a ordenação por Prioridade a padrão


- **Remover Tarefa**: Exclusão via ID.


- **Validação de Dados**: Tratamento de exceções para entradas inválidas, e uso de números para facilitar a identificação e seleção de uma opção pelo usuário

## Mais informações

- Java JDK 17.0.18
- Segue o padrão Git Flow para organização do desenvolvimento do projeto
- Estrutura do projeto:
    - Package `Model`: destinado a definição das entidades, como Task e Status
    - Package `Service`: destinado a lógica de manipulação das tarefas e lista de tarefas
    - `View/Menu`: destinado a interação com o usuário
    - `Main`: ponto de entrada da aplicação