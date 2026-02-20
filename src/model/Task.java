package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Cloneable{
    private Integer id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private int priority;
    private Status status;
    private Category category;

    public Task(int id, String title, int priority, Status status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

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
        StringBuilder task = new StringBuilder();
        task.append("> Tarefa:");
        task.append(" ID = ").append(id);
        task.append(", Título = ").append(title);

        if (description == null || description.isEmpty()) {
            task.append(", Descrição = --- ");
        } else {
            task.append(", Descrição = ").append(description);
        }

        if (dueDate == null) {
            task.append(", Data de término = --- ");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDueDate = dueDate.format(formatter);
            task.append(", Data de término = ").append(formattedDueDate);
        }

        task.append(", Prioridade = ").append(priority);

        switch (status) {
            case TODO:
                task.append(", Status = Pendente");
                break;
            case DOING:
                task.append(", Status = Em progresso");
                break;
            case DONE:
                task.append(", Status = Concluído");
                break;
            default:
                // inválido
        }

        if (category == null) {
            task.append(", Categoria = --- ");
        } else {
            task.append(", Categoria = ").append(category);
        }
        return String.valueOf(task);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}