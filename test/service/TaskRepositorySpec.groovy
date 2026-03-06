package service

import exception.TaskNotFoundException
import model.Category
import model.Status
import model.Task
import repository.TaskRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class TaskRepositorySpec extends Specification {

    @Subject
    TaskRepository taskRepository

    def setup() {
        taskRepository = new TaskRepository()
    }

    // TESTES CREATE ######################################################

    def "deve adicionar uma tarefa a lista"() {
        given: "uma tarefa"
        Task task = new Task("titulo1", 5, Status.TODO)

        when: "chamar o repository para adicionar a tarefa na lista"
        taskRepository.add(task)

        then: "deve ter um item na lista"
        taskRepository.getAllTasks().size() == 1

        and: "deve ter gerado o id 1"
        taskRepository.getAllTasks().get(0).getId() == 1

    }

    def "deve adicionar uma tarefa e manter a lista ordenada"() {
        given: "tarefas com diferentes prioridades"
        Task task1 = new Task("titulo1", 5, Status.TODO)
        Task task2 = new Task("titulo2", 2, Status.TODO)
        Task task3 = new Task("titulo3", 3, Status.TODO)

        when: "são adicionadas a lista"
        taskRepository.add(task1)
        taskRepository.add(task2)
        taskRepository.add(task3)

        then: "a lista deve estar ordenada"
        def tasks = taskRepository.getAllTasks()
        tasks[0].getPriority() == 2
        tasks[1].getPriority() == 3
        tasks[2].getPriority() == 5
    }

    // TESTES UPDATE ######################################################

    def "deve atualizar uma tarefa da lista"() {
        given: "lista com uma tarefa"
        Task task = new Task("titulo1", 5, Status.TODO)
        taskRepository.add(task)

        and: "a tarefa com os novos dados"
        Task updatedTask = new Task(task.getId(), "titulo1", 3, Status.DONE)

        when: "chamar o repository para atualizar a tarefa da lista"
        taskRepository.update(updatedTask)

        then: "a lista deve ter um item"
        taskRepository.getAllTasks().size() == 1

        and: "deve ter atualizado os dados"
        taskRepository.findById(task.getId()).getStatus() == Status.DONE
        taskRepository.findById(task.getId()).getPriority() == 3
    }

    // TESTES DELETE ######################################################

    def "deve remover uma tarefa da lista"() {
        given: "lista com uma tarefa"
        Task task = new Task("titulo1", 5, Status.TODO)
        taskRepository.add(task)

        when: "chamar o repository para remover a tarefa da lista"
        taskRepository.remove(task)

        then: "a lista deve estar vazia"
        taskRepository.getAllTasks().size() == 0
    }
}