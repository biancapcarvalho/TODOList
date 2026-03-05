package service

import exception.TaskNotFoundException
import model.Category
import model.Status
import model.Task
import repository.TaskRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class TaskServiceSpec extends Specification {

    TaskRepository taskRepository
    @Subject
    TaskService taskService

    def setup() {
        taskRepository = Mock(TaskRepository)
        taskService = new TaskService(taskRepository)
    }

    // TESTES CREATE ######################################################

    def "deve criar uma tarefa com sucesso"() {
        given: "dados válidos"
        String title = "Algum titulo para a tarefa"
        int priority = 1
        Status status = Status.TODO
        String description = "Alguma descrição para a nova tarefa"
        LocalDate dueDate = LocalDate.now()
        Category category = Category.FACULDADE

        when: "chamar o service para criar a tarefa"
        Task createdTask = taskService.createTask(title, priority, status, description, dueDate, category)

        then: "o service deve ter chamado o add do repository uma vez"
        1 * taskRepository.add(_ as Task) >> { Task task ->
            task.setId(1)
            return task
        }

        and: "os atributos devem corresponder aos dados enviados"
        createdTask != null
        createdTask.getId() == 1
        createdTask.getTitle() == title
        createdTask.getPriority() == priority
        createdTask.getStatus() == status
        createdTask.getDescription() == description
        createdTask.getDueDate() == dueDate
        createdTask.getCategory() == category
    }

    def "deve impedir a criação de tarefa com dado obrigatório inválido"() {
        given: "algum dado obrigatório inválido"
        String description = "Alguma descrição para a nova tarefa"
        LocalDate dueDate = LocalDate.now()
        Category category = Category.FACULDADE

        when: "chamar o service para criar a tarefa"
        taskService.createTask(title, priority, status, description, dueDate, category)

        then: "uma exceção deve ser retornada"
        def exception = thrown(IllegalArgumentException)

        and: "mensagem de erro"
        exception.message == errorMsg

        and: "o repository não deve ser chamado"
        0 * taskRepository.add(_)

        where: "cenarios"
        title    | priority | status        | errorMsg
        ""       | 1        | Status.TODO   | "ERRO: Título inválido!"
        null     | 5        | Status.DOING  | "ERRO: Título inválido!"
        "valido" | 6        | Status.DONE   | "ERRO: Prioridade inválida!"
        "valido" | 0        | Status.DONE   | "ERRO: Prioridade inválida!"
        "valido" | 1        | null          | "ERRO: Status inválido!"
    }

    // TESTES READ ######################################################

    def "deve receber um ID e retornar a tarefa correspondente"() {
        given: "uma lista de tarefas"
        Task task = new Task(2, "titulo2", 5, Status.DONE)

        when: "chamar o service para ler a tarefa de ID 2"
        def taskFound = taskService.findById(2)

        then: "o service deve ter chamado o findById do repository uma vez"
        1 * taskRepository.findById(2) >> task

        and: "os atributos devem corresponder aos dados enviados"
        taskFound != null
        taskFound.getId() == task.getId()
        taskFound.getTitle() == task.getTitle()
        taskFound.getPriority() == task.getPriority()
        taskFound.getStatus() == task.getStatus()
    }

    def "deve receber um ID para ler uma tarefa e lançar uma exceção"() {
        when: "chamar o service para ler a tarefa de ID 4"
        taskService.findById(4)

        then: "o service deve ter chamado o findById do repository uma vez"
        1 * taskRepository.findById(4) >> null

        and: "deve lançar uma exceção"
        thrown(TaskNotFoundException)
    }

    def "deve retornar uma lista com todas as tarefas"() {
        given: "uma lista de tarefas"
        List<Task> taskList = [
                new Task(1, "titulo1", 1, Status.DOING),
                new Task(2, "titulo2", 5, Status.DONE),
                new Task(3, "titulo3", 3, Status.TODO)
        ]

        when: "chamar o service para ler todas as tarefas"
        def taskListFound = taskService.getAllTasks()

        then: "o service deve ter chamado o getAllTasks do repository uma vez"
        1 * taskRepository.getAllTasks() >> taskList

        and: "deve retornar uma lista com 3 elementos"
        taskListFound.size() == 3
        taskListFound == taskList
    }

    def "deve retornar uma lista vazia"() {
        given: "uma lista de tarefas vazia"
        List<Task> taskList = []

        when: "chamar o service para ler todas as tarefas"
        def taskListFound = taskService.getAllTasks()

        then: "o service deve ter chamado o getAllTasks do repository uma vez"
        1 * taskRepository.getAllTasks() >> taskList

        and: "deve retornar uma vazia"
        taskListFound.size() == 0
        taskListFound == taskList
    }

    // TESTES UPDATE ######################################################

    def "deve receber uma tarefa e atualiza-la"() {
        given: "uma tarefa"
        Task originalTask = new Task(2, "titulo2", 5, Status.DONE)
        Task newDataTask = new Task(2, "novo titulo2", 3, Status.DOING)

        when: "chamar o service para atualizar a tarefa"
        taskService.updateTask(newDataTask)

        then: "o service deve ter chamado o findById do repository uma vez"
        1 * taskRepository.findById(2) >> originalTask

        and: "o service deve ter chamado o update do repository uma vez"
        1 * taskRepository.update(originalTask)

        and: "os dados da tarefa foram atualizados"
        originalTask.getTitle() == newDataTask.getTitle()
        originalTask.getPriority() == newDataTask.getPriority()
        originalTask.getStatus() == newDataTask.getStatus()
        originalTask.getDescription() == newDataTask.getDescription()
        originalTask.getDueDate() == newDataTask.getDueDate()
        originalTask.getCategory() == newDataTask.getCategory()
    }

    def "deve receber uma tarefa para atualizar e lançar uma exceção"() {
        given: "uma tarefa"
        Task task = new Task(2, "titulo2", 5, Status.DONE)

        when: "chamar o service para atualizar a tarefa"
        taskService.updateTask(task)

        then: "o service deve ter chamado o findById do repository uma vez"
        1 * taskRepository.findById(2) >> null

        and: "o service não deve ter chamado o update do repository"
        0 * taskRepository.update(_)

        and: "deve lançar uma exceção"
        thrown(TaskNotFoundException)
    }

    def "deve receber uma tarefa e lançar uma exceção por campo obrigatório inválido"() {
        given: "uma tarefa"
        Task originalTask = new Task(2, "titulo2", 5, Status.DONE)
        Task newDataTask = new Task(2, title, priority, status)

        when: "chamar o service para atualizar a tarefa"
        taskService.updateTask(newDataTask)

        then: "o service deve ter chamado o findById do repository uma vez"
        1 * taskRepository.findById(2) >> originalTask

        and: "uma exceção deve ser retornada"
        def exception = thrown(IllegalArgumentException)

        and: "mensagem de erro"
        exception.message == errorMsg

        and: "o service não deve ter chamado o update do repository"
        0 * taskRepository.update(_)

        and: "os dados da tarefa não foram atualizados"
        originalTask.getTitle() == "titulo2"
        originalTask.getPriority() == 5
        originalTask.getStatus() == Status.DONE
        originalTask.getDescription() == null
        originalTask.getDueDate() == null
        originalTask.getCategory() == null

        where: "cenarios"
        title    | priority | status        | errorMsg
        ""       | 1        | Status.TODO   | "ERRO: Título inválido!"
        null     | 4        | Status.DOING  | "ERRO: Título inválido!"
        "valido" | 6        | Status.TODO   | "ERRO: Prioridade inválida!"
        "valido" | 0        | Status.DOING  | "ERRO: Prioridade inválida!"
        "valido" | 1        | null          | "ERRO: Status inválido!"
    }

}