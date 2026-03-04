package service

import model.Category
import model.Status
import model.Task
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class TaskServiceSpec extends Specification {

    @Subject
    TaskService taskService

    def setup() {
        taskService = new TaskService()
    }

    // TESTES CREATE ######################################################

    def "criar tarefa com sucesso"() {
        given: "dados válidos"
        String title = "Algum titulo para a tarefa"
        int priority = 1
        Status status = Status.TODO
        String description = "Alguma descrição para a nova tarefa"
        LocalDate dueDate = LocalDate.now()
        Category category = Category.FACULDADE

        when: "chamar o service para criar a tarefa"
        Task createdTask = taskService.createTask(title, priority, status, description, dueDate, category)
        println createdTask

        then: "a tarefa retornada não deve ser nula"
        createdTask != null

        and: "os atributos devem corresponder aos dados enviados"
        createdTask.getTitle() == title
        createdTask.getPriority() == priority
        createdTask.getStatus() == status
        createdTask.getDescription() == description
        createdTask.getDueDate() == dueDate
        createdTask.getCategory() == category

        and: "deve ter sido gerado o ID 1"
        createdTask.getId() == 1
    }

    def "não permitir a criação de tarefa com dado obrigatório inválido (passando tudo)"() {
        given: "algum dado obrigatório inválido"
        String description = "Alguma descrição para a nova tarefa"
        LocalDate dueDate = LocalDate.now()
        Category category = Category.FACULDADE

        when: "chamar o service para criar a tarefa"
        taskService.createTask(title, priority, status, description, dueDate, category)

        then: "uma exceção deve ser lançada"
        def exception = thrown(IllegalArgumentException)

        and: "mensagem de erro"
        exception.message == errorMsg

        where: "cenarios"
        title    | priority | status        | errorMsg
        ""       | 1        | Status.TODO   | "ERRO: Título inválido!"
        null     | 5        | Status.DOING  | "ERRO: Título inválido!"
        "valido" | 6        | Status.DONE   | "ERRO: Prioridade inválida!"
        "valido" | 0        | Status.DONE   | "ERRO: Prioridade inválida!"
        "valido" | 1        | null          | "ERRO: Status inválido!"
    }
}