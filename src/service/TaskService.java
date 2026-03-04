package service;

import model.Category;
import model.Status;
import model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskService {
    List<Task> taskList = new ArrayList<>();
    int lastId = 0;

    // criar tarefa somente com os campos obrigatórios (criado a partir do teste)
    public Task createTask(String title, int priority, Status status) {
        validadeDataTask(title, priority, status);

        int id = generateId();

        Task newTask = new Task(id, title, priority, status);
        taskList.add(newTask);
        taskList.sort(Comparator.comparing(Task::getPriority));
        return newTask;
    }

    // criar tarefa com todos os campos (criado a partir do teste)
    public Task createTask(String title, int priority, Status status, String description, LocalDate dueDate, Category category) {
        validadeDataTask(title, priority, status);

        int id = generateId();

        Task newTask = new Task(id, title, priority, status);

        if (description != null) {
            newTask.setDescription(description);
        }

        if (dueDate != null) {
            newTask.setDueDate(dueDate);
        }

        if (category != null) {
            newTask.setCategory(category);
        }

        taskList.add(newTask);
        taskList.sort(Comparator.comparing(Task::getPriority));

        return newTask;
    }

    public void validadeDataTask(String title, int priority, Status status) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("ERRO: título inválido");
        }

        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("ERRO: prioridade inválida");
        }

        if (status == null) {
            throw new IllegalArgumentException("ERRO: status inválido");
        }
    }

    public Task getTask(int id) {
        return taskList.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public List<Task> getTaskByStatus(Status status) {
        return taskList.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> getTaskByPriority(int a, int b) {
        return taskList.stream()
                .filter(task -> task.getPriority() >= a && task.getPriority() <= b) // b >= p >= a
                .collect(Collectors.toList());
    }

    public List<Task> getTaskByCategory(Category category) {
        return taskList.stream()
                .filter(task -> Objects.equals(task.getCategory(), category))
                .collect(Collectors.toList());
    }

    public boolean findIfExists(int id) {
        for (Task task : taskList) {
            int taskId = task.getId();
            if (taskId == id) {
                return true;
            }
        }

        return false;
    }

    public void deleteTask(int taskId) {
        Task task = getTask(taskId);
        taskList.remove(task);
    }

    public void updateTask(Task task) {
        Task taskToUpdate = getTask(task.getId());
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setCategory(task.getCategory());
        taskList.sort(Comparator.comparing(Task::getPriority));
    }

    private int generateId() {
        int id = lastId + 1;
        lastId = id;
        return id;
    }

    public boolean isEmptyList() {
        return taskList.isEmpty();
    }
}
