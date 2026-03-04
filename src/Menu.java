import model.Category;
import model.Status;
import model.Task;
import repository.TaskRepository;
import service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    TaskService taskService = new TaskService(new TaskRepository());

    public void showMenu() {
        int option = 0;

        while (option != 5) {
            boolean invalidOption = true;

            while (invalidOption) {
                System.out.println("\n######## MENU ########\n");
                System.out.println("1 - Ver tarefas");
                System.out.println("2 - Criar tarefa");
                System.out.println("3 - Editar tarefa");
                System.out.println("4 - Remover tarefa");
                System.out.println("5 - Sair");
                System.out.print("\n## Digite o número da opção: ");
                try {
                    option = Integer.parseInt(scanner.nextLine());

                    if (option > 5 || option <= 0) {
                        System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção do menu você deve inserir o número correspondente a essa opção.");
                    } else {
                        invalidOption = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção do menu você deve inserir o número correspondente a essa opção.");
                }
            }

            switch (option) {
                case 1:
                    handleReadTasks();
                    break;
                case 2:
                    handleCreateTask();
                    break;
                case 3:
                    handleUpdateTask();
                    break;
                case 4:
                    handleDeleteTask();
                    break;
                case 5:
                    System.out.println("\nAté mais");
                    break;
                default:
                    // já tratei os demais casos no try-catch
            }
        }
    }

    public void handleReadTasks() {
        if (taskService.isEmptyList()) {
            System.out.println("\n   -- Você não tem tarefas cadastradas.");
        } else {
            filterMenu();
        }
    }

    private void filterMenu() {
        System.out.println("\n### Filtrar lista de tarefas por: ###");
        System.out.println("1 - Status");
        System.out.println("2 - Prioridade");
        System.out.println("3 - Categoria");
        System.out.println("[Enter para não filtrar lista]");
        System.out.print("\n## Digite o número da opção: ");
        String input = scanner.nextLine();

        int orderBy;

        if (input.trim().isEmpty()) {
            showTasks(taskService.getAllTasks());
        } else {
            orderBy = Integer.parseInt(input);
            if (orderBy > 3 || orderBy < 1) {
                showTasks(taskService.getAllTasks());
            } else {
                if (orderBy == 1) {
                    statusFilterMenu();
                } else if (orderBy == 2) {
                    priorityFilterMenu();
                } else {
                    categoryFilterMenu();
                }
            }
        }
    }

    private void statusFilterMenu() {
        System.out.println("\n### Informe por qual status deseja filtrar a lista");
        System.out.println("1 - Pendente");
        System.out.println("2 - Em progresso");
        System.out.println("3 - Concluído");
        System.out.println("[Enter para não filtrar lista]");
        System.out.print("\n## Digite o número da opção: ");
        String input = scanner.nextLine();

        int status;

        if (input.trim().isEmpty()) {
            showTasks(taskService.getAllTasks());
        } else {
            status = Integer.parseInt(input);
            if (status > 3 || status < 1) {
                showTasks(taskService.getAllTasks());
            } else {
                if (status == 1) {
                    showTasks(taskService.getTaskByStatus(Status.TODO));
                } else if (status == 2) {
                    showTasks(taskService.getTaskByStatus(Status.DOING));
                } else {
                    showTasks(taskService.getTaskByStatus(Status.DONE));
                }
            }
        }
    }


    private void priorityFilterMenu() {
        System.out.println("\n### Informe por qual intervalo de prioridade deseja filtrar a lista");
        System.out.println("Instruções (considere x e y números entre de 1 a 5):");
        System.out.println("x: mostra as tarefas de prioridade x");
        System.out.println("x-y: mostra as tarefas de prioridade x a y");
        System.out.println(">x: mostra as tarefas de prioridade maior ou igual a x");
        System.out.println("<x: mostra as tarefas de prioridade menor ou igual a x");
        System.out.println("[Enter para não filtrar lista]");
        System.out.print("\n## Digite o intervalo seguindo o padrão das instruções: ");
        String input = scanner.nextLine();

        if (input.trim().isEmpty()) {
            showTasks(taskService.getAllTasks());
        } else {
            input = input.trim().replaceAll("\\s", "");
            StringBuilder str = new StringBuilder(input);
            if (str.indexOf("<") != -1) {
                int i = str.indexOf("<");
                str.delete(i, i+1);
                if (str.length() != 1) {
                    showTasks(taskService.getAllTasks());
                } else {
                    int p = Integer.parseInt(String.valueOf(str));
                    showTasks(taskService.getTaskByPriority(0,p)); // > 0 && < p
                }
            } else if (str.indexOf(">") != -1) {
                int i = str.indexOf(">");
                str.delete(i, i+1);
                if (str.length() != 1) {
                    showTasks(taskService.getAllTasks());
                } else {
                    int p = Integer.parseInt(String.valueOf(str));
                    showTasks(taskService.getTaskByPriority(p,5)); // > p && < 5
                }
            } else if (str.indexOf("-") != -1) {
                StringBuilder aux = new StringBuilder(str);
                int i = str.indexOf("-");
                aux.delete(i, i+1);

                if (aux.length() != 2) {
                    showTasks(taskService.getAllTasks());
                } else {
                    int a = Integer.parseInt(str.substring(0, i));
                    int b = Integer.parseInt(str.substring(i + 1, str.length()));

                    if (a > b) {
                        int c = b;
                        b = a;
                        a = c;
                    }

                    showTasks(taskService.getTaskByPriority(a,b));
                }
            } else {
                if (str.length() != 1) {
                    showTasks(taskService.getAllTasks());
                } else {
                    int p = Integer.parseInt(input);
                    showTasks(taskService.getTaskByPriority(p,p));
                }
            }
        }
    }

    private void categoryFilterMenu() {
        System.out.println("\n### Informe por qual categoria deseja filtrar a lista");
        System.out.println("1 - Casa");
        System.out.println("2 - Faculdade");
        System.out.println("3 - Trabalho");
        System.out.println("4 - Sem categoria");
        System.out.println("[Enter para não filtrar lista]");
        System.out.print("\n## Digite o número da opção: ");
        String input = scanner.nextLine();

        int category;

        if (input.trim().isEmpty()) {
            showTasks(taskService.getAllTasks());
        } else {
            category = Integer.parseInt(input);
            if (category > 4 || category < 1) {
                showTasks(taskService.getAllTasks());
            } else {
                if (category == 1) {
                    showTasks(taskService.getTaskByCategory(Category.CASA));
                } else if (category == 2) {
                    showTasks(taskService.getTaskByCategory(Category.FACULDADE));
                } else if (category == 3) {
                    showTasks(taskService.getTaskByCategory(Category.TRABALHO));
                } else {
                    showTasks(taskService.getTaskByCategory(null));
                }
            }
        }
    }

    public void showTasks(List<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println("\n   -- Sem tarefas");
        } else {
            System.out.println("\n--LISTA DE TAREFAS--------------------------------------------------------------------------------------\n");
            for (Task task : taskList) {
                System.out.println(task);
            }
            System.out.println("\n--------------------------------------------------------------------------------------------------------\n");
        }
    }

    public void handleCreateTask() {
        taskService.createTask(
                getTitleInput(),
                getPriorityInput(),
                getStatusInput(),
                getDescriptionInput(),
                getDueDateInput(),
                getCategoryInput()
        );

        // todo - metodo para exibir somente a tarefa criada
    }

    public void handleUpdateTask() {
        if (taskService.isEmptyList()) {
            System.out.println("\n   -- Você não tem tarefas cadastradas.");
        } else {
            int id = getIdInput();

            if (taskService.exists(id)) {
                Task originalTask = taskService.findById(id);
                Task taskClone = originalTask.clone();
                int option = 0;

                while(option != 7 && option != 8) {
                    boolean invalidOption = true;
                    System.out.println("\n############ ATUALIZAR TAREFA ############");
                    while (invalidOption) {
                        System.out.println("\nOrientações para edição:\n - para manter o valor atual do atributo tecle Enter,\n - para alterar digite o novo valor,\n - para remover insira '-' e tecle Enter [válido para campos opcionais]\n");
                        System.out.println("Dados atuais da tarefa: " + taskClone);

                        System.out.println("\nInforme qual campo você deseja alterar\n");
                        System.out.println("1 - Título");
                        System.out.println("2 - Descrição");
                        System.out.println("3 - Data de término");
                        System.out.println("4 - Prioridade");
                        System.out.println("5 - Status");
                        System.out.println("6 - Categoria");
                        System.out.println("7 - Salvar e sair");
                        System.out.println("8 - Sair sem salvar");
                        System.out.print("\n## Digite o número da opção: ");

                        try {
                            option = Integer.parseInt(scanner.nextLine());

                            if (option > 8 || option <= 0) {
                                System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção do menu você deve inserir o número correspondente a essa opção.");
                            } else {
                                invalidOption = false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção do menu você deve inserir o número correspondente a essa opção.");
                        }
                    }

                    switch (option) {
                        case 1:
                            String title = getUpdatedTitleInput(taskClone.getTitle());
                            taskClone.setTitle(title);
                            break;
                        case 2:
                            String description = getUpdatedDescriptionInput(taskClone.getDescription());
                            taskClone.setDescription(description);
                            break;
                        case 3:
                            LocalDate dueDate = getUpdatedDueDateInput(taskClone.getDueDate());
                            taskClone.setDueDate(dueDate);
                            break;
                        case 4:
                            int priority = getUpdatedPriorityInput(taskClone.getPriority());
                            taskClone.setPriority(priority);
                            break;
                        case 5:
                            Status status = getUpdatedStatusInput(taskClone.getStatus());
                            taskClone.setStatus(status);
                            break;
                        case 6:
                            Category category = getUpdatedCategoryInput(taskClone.getCategory());
                            taskClone.setCategory(category);
                            break;
                        case 7:
                            //salvar e sair
                            taskService.updateTask(taskClone);
                            break;
                        case 8:
                            // sair sem salvar
                            break;
                    }
                }

            } else {
                System.out.println("   -- ATENÇÃO: ID inválido.");
                // aborta - volta pro menu
            }
        }
    }

    public void handleDeleteTask() {
        if (taskService.isEmptyList()) {
            System.out.println("\n   -- Você não tem tarefas cadastradas.");
        } else {
            System.out.println("\n############ REMOVER TAREFA ############");

            taskService.deleteTask(getIdInput());

            // todo - método para exibir confirmação de remoção da tarefa
        }
    }

    private int getIdInput() {
        showTasks(taskService.getAllTasks());

        int taskId = 0;
        boolean invalidId = true;

        while (invalidId) {
            System.out.print("\n## Informe o ID da tarefa: ");

            try {
                taskId = Integer.parseInt(scanner.nextLine());

                if (taskService.exists(taskId)) {
                    invalidId = false;
                } else {
                    System.out.println("   -- ATENÇÃO: ID inválido. Você deve inserir o número do ID correspondente a tarefa.");
                }
            } catch (NumberFormatException e) {
                System.out.println("   -- ATENÇÃO: ID inválido. Você deve inserir o número do ID correspondente a tarefa.");
            }
        }

        return taskId;
    }

    /**
     * FUNÇÕES AUXILIARES PARA CRIAR TAREFA
     */
    public String getTitleInput() {
        String title;
        while (true) {
            System.out.print("\n## Informe o título da tarefa: ");
            title = scanner.nextLine();

            if (!title.trim().isEmpty()) break;
            System.out.println("   -- ATENÇÃO: O título é um campo obrigatório.");
        }

        return title;
    }

    public String getDescriptionInput() {
        System.out.print("\n## Informe a descrição da tarefa: ");

        return scanner.nextLine();
    }

    public LocalDate getDueDateInput() {
        boolean invalidDate = true;
        LocalDate dueDate = null;

        while (invalidDate) {
            System.out.print("\n## Informe a data de término (Formato: DD/MM/AAAA, ex: 13/02/2026): ");
            try {
                String stringDueDate = scanner.nextLine();

                if (!stringDueDate.trim().isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dueDate = LocalDate.parse(stringDueDate, formatter);
                    invalidDate = false;
                } else {
                    invalidDate = false;
                }
            } catch (DateTimeParseException e) {
                System.out.println("\n   -- ATENÇÃO: Data inválida. Formato aceito: DD/MM/AAAA, ex: 13/02/2026.\n");
            }
        }
        return dueDate;
    }

    public int getPriorityInput() {
        int priority = 0;
        boolean invalidPriority = true;

        while (invalidPriority) {
            System.out.print("\n## Informe a prioridade da tarefa [1 a 5]: ");

            try {
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    priority = Integer.parseInt(input);
                }

                if (priority > 5 || priority < 1) {
                    System.out.println("\n   -- ATENÇÃO: Prioridade inválida. Valores aceitos: 1, 2, 3, 4, 5.\n");
                } else {
                    invalidPriority = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n   -- ATENÇÃO: Prioridade inválida. Valores aceitos: 1, 2, 3, 4, 5.\n");
            }
        }

        return priority;
    }

    public Status getStatusInput() {
        int status = 0;
        boolean invalidStatus = true;

        while (invalidStatus) {
            System.out.println("\n### Status da tarefa ###");
            System.out.println("1 - Pendente");
            System.out.println("2 - Em progresso");
            System.out.println("3 - Concluído");
            System.out.print("\n## Digite o número da opção: ");

            try {
                status = Integer.parseInt(scanner.nextLine());

                if (status > 3 || status < 1) {
                    System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                } else {
                    invalidStatus = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
            }
        }

        return switch (status) {
            case 2 -> Status.DOING;
            case 3 -> Status.DONE;
            default -> Status.TODO;
        };
    }

    public Category getCategoryInput() {
        int category = 0;
        boolean invalidCategory = true;

        while (invalidCategory) {
            System.out.println("\n### Categoria da tarefa ###");
            System.out.println("1 - Casa");
            System.out.println("2 - Faculdade");
            System.out.println("3 - Trabalho");
            System.out.println("[Enter para não selecionar categoria]");
            System.out.print("\n## Digite o número da opção: ");

            try {
                String input = scanner.nextLine();

                if (input.trim().isEmpty()) {
                    // não informou categoria
                    invalidCategory = false;
                } else {
                    category = Integer.parseInt(input);
                    if (category > 3 || category < 1) {
                        System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                    } else {
                        invalidCategory = false;
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
            }
        }

        return switch (category) {
            case 1 -> Category.CASA;
            case 2 -> Category.FACULDADE;
            case 3 -> Category.TRABALHO;
            default -> null;
        };
    }

    /**
     * FUNÇÕES AUXILIARES PARA ATUALIZAR TAREFA
     */
    public String getUpdatedTitleInput(String title) {
        System.out.print("\n - Informe o novo título: ");
        String textInput = scanner.nextLine();

        if (textInput.trim().isEmpty()) {
            return title;
        }

        return textInput.trim();
    }

    public String getUpdatedDescriptionInput(String description) {
        System.out.print("\n - Informe a nova descrição: ");
        String textInput = scanner.nextLine();

        if (textInput.trim().isEmpty()) {
            return description;
        } else if (textInput.equals("-")) {
            return "";
        } else {
            return textInput.trim();
        }
    }

    public LocalDate getUpdatedDueDateInput(LocalDate dueDate) {
        boolean invalidDate = true;
        LocalDate newDueDate = dueDate;
        while (invalidDate) {
            System.out.print("\n - Informe a nova data de término: ");
            String textInput = scanner.nextLine();

            try {
                if (textInput.trim().isEmpty()) {
                    return dueDate;
                } else if (textInput.trim().equals("-")) {
                    return null;
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    newDueDate = LocalDate.parse(textInput, formatter);
                    invalidDate = false;
                }
            } catch (DateTimeParseException e) {
                System.out.println("\n   -- ATENÇÃO: Data inválida. Formato aceito: DD/MM/AAAA, ex: 13/02/2026.\n");
            }
        }

        return newDueDate;
    }

    public int getUpdatedPriorityInput(int priority) {
        int newPriority = 0;
        boolean invalidPriority = true;

        while (invalidPriority) {
            System.out.print("\n## - Informe a nova prioridade [1 a 5]: ");
            String input = scanner.nextLine();

            if (input.isEmpty() || input.trim().isEmpty()) {
                return priority;
            } else {
                try {
                    newPriority = Integer.parseInt(input);
                    if (newPriority > 5 || newPriority < 1) {
                        System.out.println("\n   -- ATENÇÃO: Prioridade inválida. Valores aceitos: 1, 2, 3, 4, 5.\n");
                    } else {
                        invalidPriority = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n   -- ATENÇÃO: Prioridade inválida. Valores aceitos: 1, 2, 3, 4, 5.\n");
                }
            }
        }

        return newPriority;
    }

    public Status getUpdatedStatusInput(Status status) {
        int newStatus = 0;
        boolean invalidStatus = true;

        while (invalidStatus) {
            System.out.print("\n - Informe o novo status [1. Pendente, 2. Em progresso, 3. Concluído]: ");
            String input = scanner.nextLine();

            if (input.isEmpty() || input.trim().isEmpty()) {
                return status;
            } else {
                try {
                    newStatus = Integer.parseInt(input);
                    if (newStatus > 3 || newStatus < 1) {
                        System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                    } else {
                        invalidStatus = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                }
            }
        }

        return switch (newStatus) {
            case 1 -> Status.TODO;
            case 2 -> Status.DOING;
            case 3 -> Status.DONE;
            default -> status;
        };
    }

    public Category getUpdatedCategoryInput(Category category) {
        int newCategory = 0;
        boolean invalidCategory = true;

        while (invalidCategory) {
            System.out.print("\n - Informe a nova categoria [1. Casa, 2. Faculdade, 3. Trabalho]: ");
            String textInput = scanner.nextLine();

            if (textInput.trim().isEmpty()) {
                return category;
            } else if (textInput.equals("-")) {
                return null;
            } else {
                try {
                    newCategory = Integer.parseInt(textInput);
                    if (newCategory > 3 || newCategory < 1) {
                        System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                    } else {
                        invalidCategory = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n   -- ATENÇÃO: Opção inválida. Para selecionar uma opção você deve inserir o número correspondente a essa opção.");
                }
            }
        }

        return switch (newCategory) {
            case 1 -> Category.CASA;
            case 2 -> Category.FACULDADE;
            case 3 -> Category.TRABALHO;
            default -> category;
        };
    }
}
