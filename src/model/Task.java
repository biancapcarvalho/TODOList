package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    // atributos
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private int priority;
    private Status status;
    private Category category;

    // construtor
    public Task(int id, String title, int priority, Status status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    // métodos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        String task = "> Tarefa: " +
                "ID = " + id +
                ", Título = " + title;

        if (description == null || description.isEmpty()) {
            task = task + ", Descrição = --- ";
        } else {
            task = task + ", Descrição = " + description;
        }

        if (dueDate == null) {
            task = task + ", Data de término = --- ";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDueDate = dueDate.format(formatter);
            task = task + ", Data de término = " + formattedDueDate;
        }

        task = task + ", Prioridade = " + priority;

        switch (status) {
            case TODO:
                task = task + ", Status = A fazer";
                break;
            case DOING:
                task = task + ", Status = Em progresso";
                break;
            case DONE:
                task = task + ", Status = Concluído";
                break;
            default:
                // inválido
        }

        if (category == null) {
            task = task + ", Categoria = --- ";
        } else {
            task = task + ", Categoria = " + category;
        }

        task = task + " }";

        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}