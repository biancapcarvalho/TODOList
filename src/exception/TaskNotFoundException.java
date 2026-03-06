package exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public TaskNotFoundException(int id) {
        super("Tarefa não encontrada com o ID: " + id);
    }
}