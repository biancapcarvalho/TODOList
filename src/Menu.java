import model.Category;
import model.Status;
import model.Task;
import service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    TaskService taskService = new TaskService();

    public void showMenu() {
        int option = 0;

        do {
            boolean invalidOption = true;

            while (invalidOption) {
                System.out.println("\n######## MENU ########\n");
                System.out.println("1 - Ver tarefas");
                System.out.println("2 - Criar tarefa");
                System.out.println("3 - Remover tarefa");
                System.out.println("4 - Sair");
                System.out.print("\n## Digite o número da opção: ");
                try {
                    option = Integer.parseInt(scanner.nextLine());

                    if (option > 4 || option <= 0) {
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
                    handleReadTasks(0);
                    break;
                case 2:
                    handleCreateTask();
                    break;
                case 3:
                    handleDeleteTask();
                    break;
                case 4:
                    System.out.println("\nAté mais");
                    break;
                default:
                    // já tratei os demais casos no try-catch
            }
        } while (option != 4);
    }

    public void handleReadTasks(int orderBy) {
        if (taskService.isEmptyList()) {
            System.out.println("\n   -- Você não tem tarefas cadastradas.");
        } else if (orderBy == 0) {
            System.out.println("\n### Ordenar lista de tarefas por: ###");
            System.out.println("1 - Status");
            System.out.println("2 - Prioridade");
            System.out.println("3 - Categoria");
            System.out.println("[Enter para não ordenar lista]");
            System.out.print("\n## Digite o número da opção: ");
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {
                showTasks(taskService.getAllTasks());
            } else {
                orderBy = Integer.parseInt(input);
                if (orderBy > 3 || orderBy < 1) {
                    showTasks(taskService.getAllTasks());
                } else {
                    if (orderBy == 1) {
                        showTasks(taskService.getOrderedTaskListByStatus());
                    } else if (orderBy == 2) {
                        showTasks(taskService.getOrderedTaskListByPriority());
                    } else {
                        showTasks(taskService.getOrderedTaskListByCategory());
                    }
                }
            }
        } else {
            showTasks(taskService.getAllTasks());
        }
    }

    public void showTasks(ArrayList<Task> taskList) {
        System.out.println("\n--LISTA DE TAREFAS--------------------------------------------------------------------------------------\n");
        for (Task task : taskList) {
            System.out.println(task);
        }
        System.out.println("\n--------------------------------------------------------------------------------------------------------\n");
    }

    public void handleCreateTask() {
        Task task = new Task(-1, getTitleInput(), getPriorityInput(), getStatusInput());
        task.setDescription(getDescriptionInput());
        task.setCategory(getCategoryInput());
        task.setDueDate(getDueDateInput());
        taskService.createTask(task);

        handleReadTasks(1);
    }

    public void handleDeleteTask() {
        taskService.deleteTask(getIdInput());

        handleReadTasks(1);
    }

    private int getIdInput() {
        handleReadTasks(1);

        int taskId = 0;
        boolean invalidId = true;

        while (invalidId) {
            System.out.print("\n## Informe o ID da tarefa: ");

            try {
                taskId = Integer.parseInt(scanner.nextLine());

                if (taskService.findIfExists(taskId)) {
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

    public String getTitleInput() {
        // campo obrigatório
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
        // campo opcional
        System.out.print("\n## Informe a descrição da tarefa: ");

        return scanner.nextLine();
    }

    public int getPriorityInput() {
        // campo obrigatório
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
        // campo obrigatório
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
        // campo opcional
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

    public LocalDate getDueDateInput() {
        // campo opcional
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
                    invalidDate = false; // se não informou nada, pular
                }
            } catch (DateTimeParseException e) {
                System.out.println("\n   -- ATENÇÃO: Data inválida. Formato aceito: DD/MM/AAAA, ex: 13/02/2026.\n");
            }
        }
        return dueDate;
    }
}
