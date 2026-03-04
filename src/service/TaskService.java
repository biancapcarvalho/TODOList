package service;

import model.Category;
import model.Status;
import model.Task;
import repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(String title, int priority, Status status, String description, LocalDate dueDate, Category category) {
        validateDataTask(title, priority, status);

        Task newTask = new Task(title, priority, status);

        if (description != null) {
            newTask.setDescription(description);
        }

        if (dueDate != null) {
            newTask.setDueDate(dueDate);
        }

        if (category != null) {
            newTask.setCategory(category);
        }

        repository.add(newTask);

        return newTask;
    }

    public void validateDataTask(String title, int priority, Status status) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("ERRO: Título inválido!");
        }

        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("ERRO: Prioridade inválida!");
        }

        if (status == null) {
            throw new IllegalArgumentException("ERRO: Status inválido!");
        }
    }

    public void deleteTask(int taskId) {
        repository.remove(taskId);
    }

    public boolean isEmptyList() {
        return repository.isEmpty();
    }

    public List<Task> getAllTasks() {
        return repository.getAllTasks();
    }


    public List<Task> getTaskByStatus(Status status) {
        return repository.getTaskByStatus(status);
    }

    public List<Task> getTaskByPriority(int priorityMin, int priorityMax) {
        return repository.getTaskByPriority(priorityMin, priorityMax);
    }

    public List<Task> getTaskByCategory(Category category) {
        return repository.getTaskByCategory(category);
    }

    public void updateTask(Task newTaskData) {
        Task originalTask = repository.findById(newTaskData.getId());

        if (originalTask == null) {
            throw new IllegalArgumentException("ERRO: Tarefa de ID " + newTaskData.getId() + " não encontrada");
        }

        validateDataTask(newTaskData.getTitle(), newTaskData.getPriority(), newTaskData.getStatus());

        originalTask.setTitle(newTaskData.getTitle());
        originalTask.setDescription(newTaskData.getDescription());
        originalTask.setDueDate(newTaskData.getDueDate());
        originalTask.setPriority(newTaskData.getPriority());
        originalTask.setStatus(newTaskData.getStatus());
        originalTask.setCategory(newTaskData.getCategory());

        repository.update(originalTask);
    }

    public boolean exists(Integer id) {
        return repository.findById(id) != null;
    }

    public Task findById(Integer id) {
        return repository.findById(id);
    }
}
