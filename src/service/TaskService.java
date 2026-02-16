package service;

import model.Task;

import java.util.ArrayList;
import java.util.Comparator;

public class TaskService {
    ArrayList<Task> taskList = new ArrayList<>();
    int lastId = 0;

    public void createTask(Task task) {
        int id = generateId();

        Task newTask = new Task(id, task.getTitle(), task.getPriority(), task.getStatus());

        if (task.getDescription() != null) {
            newTask.setDescription(task.getDescription());
        }

        if (task.getDueDate() != null) {
            newTask.setDueDate(task.getDueDate());
        }

        if (task.getCategory() != null) {
            newTask.setCategory(task.getCategory());
        }

        taskList.add(newTask);
        taskList.sort(Comparator.comparing(Task::getPriority));
    }

    public Task getTask(int id) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                return task;
            }
        }

        return null;
    }

    public ArrayList<Task> getAllTasks() {
        // ordenado pelo ID
        return taskList;
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

    public ArrayList<Task> getOrderedTaskListByStatus() {
        ArrayList<Task> orderedList = new ArrayList<>(taskList);
        orderedList.sort(Comparator.comparing(Task::getStatus));
        return orderedList;
    }

    public ArrayList<Task> getOrderedTaskListByPriority() {
        ArrayList<Task> orderedList = new ArrayList<>(taskList);
        orderedList.sort(Comparator.comparing(Task::getPriority));
        return orderedList;
    }

    public ArrayList<Task> getOrderedTaskListByCategory() {
        ArrayList<Task> orderedList = new ArrayList<>(taskList);
        orderedList.sort(Comparator.comparing(Task::getCategory, Comparator.nullsLast(Comparator.naturalOrder())));
        return orderedList;
    }

    public boolean isEmptyList() {
        return taskList.isEmpty();
    }
}
