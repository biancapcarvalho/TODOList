/*
* ESTADO E DADOS
* =========================================
* */

let taskUpdatingId = 0;
let lastId = 0;
let taskList = [];

let status = ['todo', 'doing', 'done'];
let priorityIconStyle = [
    { value: 1, class: "fa-solid fa-angles-down", color: "dodgerblue" },
    { value: 2, class: "fa-solid fa-angle-down", color: "dodgerblue" },
    { value: 3, class: "fa-solid fa-equals", color: "darkorange" },
    { value: 4, class: "fa-solid fa-angle-up", color: "darkred" },
    { value: 5, class: "fa-solid fa-angles-up", color: "darkred" }
]; // prioridades e classes dos ícones que representam cada uma

/*
 * RENDERIZAÇÃO HTML
 * =========================================
 */

// -> modal do bootstrap
let taskModal;

document.addEventListener("DOMContentLoaded", function() {
    taskModal = new bootstrap.Modal(document.getElementById('taskModal'));

    taskList.sort((taskA, taskB) => taskB.priority - taskA.priority);
    renderTaskList();
});

// -> função para criar card de tarefa
function createTaskCard(task, newTask = false)  {
    let priorityStyle = priorityIconStyle.find(el => el.value === task.priority);
    let dueDateTag = `
        <p class="due-date">
            <i class="fa-regular fa-clock"></i>
            ${task.dueDate ? task.dueDate.toLocaleDateString() : ""}
        </p>
    `;
    let categoryTag = `<p class="category">${task.category}</p>`;
    let descriptionIcon = `<i class="fa-solid fa-align-left"></i>`;
    return `
        <div id="task-card-${task.id}" class="task-mini-card ${newTask ? 'highlight-card' : ''}">
            <div class="header-task">
                <p>${task.title}</p>
                <div>
                    <i id="update-task" class="fa-solid fa-expand" onclick="updateTask(${task.id})" title="Ver tarefa"></i>
                    <i id="delete-task" class="fa-solid fa-trash" onclick="deleteTask(${task.id})" title="Remover tarefa"></i>
                </div>
            </div>
            <div class="footer-task">
                <div>
                    ${task.description ? descriptionIcon : ""}
                    ${task.dueDate ? dueDateTag : ""}
                    ${task.category ? categoryTag : ""}
                </div>
                <p class="priority priority-${task.priority}">
                    <i class="${priorityStyle.class}" style="color: ${priorityStyle.color}"></i>
                </p>
            </div>
        </div>
    `;
}

// -> função para adicionar novo card de tarefa na respectiva coluna
function addTaskCardToColumn(task, newTask = false) {
    document.getElementById(`${task.status}-column`).innerHTML += createTaskCard({
        id: task.id,
        title: task.title,
        dueDate: task.dueDate,
        priority: task.priority,
        category: task.category,
        description: task.description
    }, newTask);
}

// -> função para remover card de tarefa
function removeTaskCard(id) {
    document.getElementById(`task-card-${id}`).remove();
}

// funcao para percorrer lista e renderizar os cards
function renderTaskList(newTaskId = null) {
    document.getElementById(`todo-column`).innerHTML = "";
    document.getElementById(`doing-column`).innerHTML = "";
    document.getElementById(`done-column`).innerHTML = "";
    taskList?.forEach(task => {
        (task.id === newTaskId) ? addTaskCardToColumn(task, true) : addTaskCardToColumn(task);
    })

    if (newTaskId) {
        setTimeout(() => {
            let newCard = document.getElementById(`task-card-${newTaskId}`);
            if (newCard) {
                newCard.classList.remove('highlight-card');
            }
        }, 1000);
    }
}

/*
* MANIPULAÇÃO DO FORM/MODAL
* =========================================
* */

// -> função para pegar os valores dos campos do form
function getInputData() {
    return {
        title: document.getElementById("title-input").value,
        priority: parseInt(document.getElementById("priority-input").value),
        status: document.getElementById("status-input").value,
        dueDate: document.getElementById("dueDate-input").value ? new Date(document.getElementById("dueDate-input").value + 'T00:00:00') : "",
        category: document.getElementById("category-input").value,
        description: document.getElementById("description-input").value
    }
}

// -> função para validar o form
function validateForm() {
    let isValid = true;
    let form = document.getElementById("form-task");

    form.querySelectorAll('.form-control, .form-select').forEach(input => {
        if (!input.checkValidity()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
        }
    });

    let priorityInput = document.getElementById("priority-input").value;
    let priorityBtn = document.getElementById("priorityDropdownBtn");

    if (!priorityInput) {
        priorityBtn.classList.add("is-invalid");
        isValid = false;
    } else {
        priorityBtn.classList.remove("is-invalid");
    }

    return isValid;
}

// -> função para resetar os dados do form (limpa tudo)
function clearInputData() {
    document.getElementById("title-input").value = "";
    document.getElementById('priority-input').value = "";
    document.getElementById("status-input").value = "";
    document.getElementById("dueDate-input").value = "";
    document.getElementById("category-input").value = "";
    document.getElementById("description-input").value = "";

    document.getElementById("priority-input").value = "";
    let spanBtn = document.getElementById('priority-selected-text');
    spanBtn.innerHTML = "Selecione uma prioridade...";
    spanBtn.classList.add('text-secondary');

    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
}

// -> seta valores nos campos do form (usado para atualização de tarefa)
function setInputData(task) {
    let formattedDate = "";
    if (task.dueDate) {
        let year = task.dueDate.getFullYear();
        let month = String(task.dueDate.getMonth() + 1).padStart(2, '0');
        let day = String(task.dueDate.getDate()).padStart(2, '0');
        formattedDate = `${year}-${month}-${day}`;
    }

    document.getElementById("title-input").value = task.title;
    document.getElementById("status-input").value = task.status;
    document.getElementById("dueDate-input").value = formattedDate;
    document.getElementById("category-input").value = task.category;
    document.getElementById("description-input").value = task.description;

    document.getElementById("priority-input").value = task.priority;
    let selectedItem = document.querySelector(`.dropdown-menu .dropdown-item[data-value="${task.priority}"]`);
    let spanBtn = document.getElementById('priority-selected-text');
    if (selectedItem) {
        spanBtn.innerHTML = selectedItem.innerHTML;
        spanBtn.classList.remove('text-secondary');
    }
}

/*
* EVENTOS ONCLICK DO FORM/MODAL
* =========================================
* */

// -> fechar o modal pelo x
document.querySelector("#taskModal .close-btn").onclick = function () {
    taskModal.hide();
    clearInputData();
}

// -> cancelar criação/atualização pelo botão cancelar
document.querySelector("#taskModal .btn-cancel").onclick = function () {
    taskModal.hide();
    clearInputData();
}

// -> criar tarefa (botão criar)
document.querySelector("#taskModal .btn-create").onclick = function (event) {
    event.preventDefault();

    if (!validateForm()) {
        return;
    }

    taskModal.hide();

    let newTask = getInputData();
    newTask.id = lastId + 1;
    lastId += 1;

    taskList.push(newTask);
    taskList.sort((taskA, taskB) => taskB.priority - taskA.priority );
    renderTaskList(newTask.id);
    clearInputData();
}

// -> editar tarefa (botão atualizar)
document.querySelector("#taskModal .btn-update").onclick = function (event) {
    event.preventDefault();

    if (!validateForm()) {
        return;
    }

    taskModal.hide();

    let newData = getInputData();

    taskList.forEach((task) => {
        if(task.id === taskUpdatingId) {
            task.title = newData.title;
            task.status = newData.status;
            task.priority = newData.priority;
            task.category = newData.category;
            task.dueDate = newData.dueDate;
            task.description = newData.description;
        }
    })

    taskList.sort((taskA, taskB) => taskB.priority - taskA.priority );
    renderTaskList(taskUpdatingId);
    clearInputData();
}

/*
* EVENTOS ONCLICK CARDS E COLUNAS (CRIAR, ATUALIZAR, REMOVER)
* =========================================
* */

// -> criar tarefa
for (const st of status) {
    document.getElementById(`create-${st}-task`).onclick = function () {
        taskUpdatingId = 0;
        document.getElementById("status-input").value = st;

        document.getElementById("modal-title").innerHTML = "Criar Nova Tarefa";

        document.querySelector(".btn-create").style.display = "block";
        document.querySelector(".btn-update").style.display = "none";

        taskModal.show();
    }
}

// -> remover tarefa
function deleteTask(id) {
    let index = taskList.findIndex(t => t.id === id);
    if (index !== -1) {
        taskList.splice(index,1);
        removeTaskCard(id);
    } else {
        alert("Erro ao remover tarefa!");
    }
}

// -> atualizar tarefa
function updateTask(id) {
    taskUpdatingId = id;
    document.getElementById("modal-title").innerHTML = "Atualizar Tarefa";
    document.querySelector(".btn-create").style.display = "none";
    document.querySelector(".btn-update").style.display = "block";

    let task = taskList.find(t => t.id === id);
    if (task) {
        setInputData({
            title: task.title,
            dueDate: task.dueDate,
            priority: task.priority,
            category: task.category,
            status: task.status,
            description: task.description
        });
        taskModal.show();
    } else {
        alert("Não é possível atualizar essa tarefa!");
    }
}

// -> funcionamento do dropdown que substitui o select de prioridade
document.querySelectorAll('.dropdown-menu .dropdown-item').forEach(item => {
    item.addEventListener('click', function(e) {
        e.preventDefault();

        let selectedValue = this.getAttribute('data-value');
        let selectedHTML = this.innerHTML;

        document.getElementById('priority-input').value = selectedValue;

        let spanBtn = document.getElementById('priority-selected-text');
        spanBtn.innerHTML = selectedHTML;
        spanBtn.classList.remove('text-secondary');
        document.getElementById('priorityDropdownBtn').classList.remove('is-invalid');
    });
});

