/*
* ESTADO E DADOS
* =========================================
* */

let taskUpdatingId = 0; // recebe o id da task que está sendo atualizada
let lastId = 10; // variável para setar o id (atribuição do backend, usada aqui para simular)
let taskList = []; // simular saída do getAllTasks() do backend

let status = {todo: "A FAZER", doing: "EM PROGRESSO", done: "CONCLUÍDO"}; // mapa de status
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
});

// -> função para criar card de tarefa
function createTaskCard(task)  {
    let priorityStyle = priorityIconStyle.find(el => el.value === task.priority);
    let dueDateTag = `
        <p class="due-date">
            <i class="fa-regular fa-clock"></i>
            ${task.dueDate ? task.dueDate.toLocaleDateString() : ""}
        </p>
    `;
    let categoryTag = `<p class="category">${task.category}</p>`
    return `
        <div id="task-card-${task.id}" class="task-mini-card">
            <div class="header-task">
                <p>${task.title}</p>
                <div>
                    <i id="update-task" class="fa-regular fa-pen-to-square" onclick="updateTask(${task.id})"></i>
                    <i id="delete-task" class="fa-solid fa-trash" onclick="deleteTask(${task.id})"></i>
                </div>
            </div>
            <div class="footer-task">
                <div>
                    ${task.dueDate ? dueDateTag : ""}
                    ${task.category ? categoryTag : ""}
                </div>
                <p class="priority-${task.priority}">
                    <i class="${priorityStyle.class}" style="color: ${priorityStyle.color}"></i>
                </p>
            </div>
        </div>
    `;
}

// -> função para adicionar novo card de tarefa na respectiva coluna
function addTaskCardToColumn(task) {
    document.getElementById(`${task.status}-column`).innerHTML += createTaskCard({
        id: task.id,
        title: task.title,
        dueDate: task.dueDate,
        priority: task.priority,
        category: task.category
    });
}

// -> função para remover card de tarefa
function removeTaskCard(id) {
    document.getElementById(`task-card-${id}`).remove();
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
}

// -> seta valores nos campos do form (usado para atualização de tarefa)
function setInputData(task) {
    let year = task.dueDate.getFullYear();
    let month = String(task.dueDate.getMonth() + 1).padStart(2, '0');
    let day = String(task.dueDate.getDate()).padStart(2, '0');
    let formattedDate = `${year}-${month}-${day}`;

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
    let form = document.getElementById("form-task");

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    event.preventDefault();

    taskModal.hide();

    let newTask = getInputData();
    newTask.id = lastId + 1;
    lastId += 1;

    taskList.push(newTask);
    addTaskCardToColumn(newTask);
    clearInputData();
}

// -> editar tarefa (botão atualizar)
document.querySelector("#taskModal .btn-update").onclick = function (event) {
    let form = document.getElementById("form-task");
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    event.preventDefault();

    taskModal.hide();

    let newData = getInputData();

    taskList.map((task) => {
        if(task.id === taskUpdatingId) {
            task.title = newData.title;
            task.status = newData.status;
            task.priority = newData.priority;
            task.category = newData.category;
            task.dueDate = newData.dueDate;
            task.description = newData.description;
        }
    })

    let updatedTask = taskList.find(t => t.id === taskUpdatingId);

    removeTaskCard(taskUpdatingId);
    addTaskCardToColumn(updatedTask)
    clearInputData();
}

/*
* EVENTOS ONCLICK CARDS E COLUNAS (CRIAR, ATUALIZAR, REMOVER)
* =========================================
* */

// -> criar tarefa
for (const key of Object.keys(status)) {
    document.getElementById(`create-${key}-task`).onclick = function () {
        taskUpdatingId = 0;
        document.getElementById("status-input").value = key;
        $('#status-input').trigger('change');

        document.getElementById("modal-title").innerHTML = "Criar Nova Tarefa";

        document.querySelector(".btn-create").style.display = "git logblock";
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
        console.log("index nao encontrado");
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
        console.log("tarefa nao encontrada");
    }
}

document.querySelectorAll('.dropdown-menu .dropdown-item').forEach(item => {
    item.addEventListener('click', function(e) {
        e.preventDefault();

        let selectedValue = this.getAttribute('data-value');
        let selectedHTML = this.innerHTML;

        // Atualiza o input escondido com o número (1, 2, etc)
        document.getElementById('priority-input').value = selectedValue;

        // Atualiza o visual do botão (tira a cor cinza do placeholder e coloca o ícone+texto)
        let spanBtn = document.getElementById('priority-selected-text');
        spanBtn.innerHTML = selectedHTML;
        spanBtn.classList.remove('text-secondary');
    });
});