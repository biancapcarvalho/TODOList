package repository;

import model.Category;
import model.Status;
import model.Task;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskRepository {

    private List<Task> taskList = new ArrayList<>();
    private int lastId = 0;

    public void add(Task task) {
        if (task.getId() == null) {
            task.setId(generateId());
        }
        taskList.add(task);
        taskList.sort(Comparator.comparing(Task::getPriority));
    }

    public Task findById(int id) {
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

    public boolean isEmpty() {
        return taskList.isEmpty();
    }


    public void remove(Integer taskId) {
        Task task = findById(taskId);
        taskList.remove(task);
    }

    public void update(Task task) {
        Integer taskId = task.getId();
        remove(taskId);
        add(task);
    }

    private int generateId() {
        lastId++;
        return lastId;
    }
}